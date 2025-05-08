provider "aws" {
  region = "us-east-1"
}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "main-vpc"
  }
}

resource "aws_subnet" "main_subnet" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"

  tags = {
    Name = "main-subnet"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name = "main-gateway"
  }
}

resource "aws_route_table" "rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "main-route-table"
  }
}

resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.main_subnet.id
  route_table_id = aws_route_table.rt.id
}

resource "aws_security_group" "jenkins_sg" {
  name        = "jenkins-docker-sg"
  description = "Allow ports for Jenkins and Docker App"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Allow Jenkins access"
    from_port   = 9090
    to_port     = 9090
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow Docker App access"
    from_port   = 8082
    to_port     = 8082
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "jenkins-docker-sg"
  }
}

resource "aws_instance" "ebill_app" {
  ami                         = "ami-0129865974a10c1cb" # Ubuntu 22.04 LTS (verify latest in your region)
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.main_subnet.id
  vpc_security_group_ids      = [aws_security_group.jenkins_sg.id]
  key_name                    = "ebill_key"  # Replace with your key pair name

user_data = <<-EOF
              #!/bin/bash
              set -euxo pipefail

              # Install Docker and other essentials (no Java)
              apt update -y
              apt install -y docker.io curl unzip gnupg

              # Enable Docker and add user
              systemctl enable --now docker
              usermod -aG docker ubuntu

              # Create Jenkins directory
              mkdir -p /opt/jenkins
              cd /opt/jenkins

              # Download Jenkins war directly
              curl -L -o jenkins.war https://get.jenkins.io/war-stable/2.426.1/jenkins.war

              # Create a script to start Jenkins on port 9090
              cat <<EOL > start.sh
              #!/bin/bash
              nohup java -jar /opt/jenkins/jenkins.war --httpPort=9090 > /var/log/jenkins.log 2>&1 &
              EOL

              chmod +x start.sh
              ./start.sh

              # Run Dockerized Electricity Bill Estimator app on port 8082
              docker run -d -p 8082:8082 lavanya0421/ebill-estimator:latest
EOF


  tags = {
    Name = "ebill-instance"
  }
}

