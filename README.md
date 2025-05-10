# 🔌 Electricity Bill Estimator

## Project Summary

The **Electricity Bill Estimator** is designed with a **complete DevOps workflow**—from code to 
deployment—demonstrating real-world automation, monitoring, and infrastructure management.

The user provides:
- The list of **appliances**
- The **wattage** of each appliance
- The number of **hours used per day**

Based on this input, the application calculates:
- **Total units consumed (kWh)**
- The **estimated monthly electricity bill**

This project serves as both a practical **utility tool** and a **DevOps portfolio project** showcasing:
- CI/CD pipelines
- Containerization
- Orchestration
- Infrastructure as Code
- Monitoring 

## Tools & Technologies Used

| Category          | Tool / Technology          |
|-------------------|----------------------------|
| Backend Language  | Java (Spring Boot)         |
| Frontend UI       | HTML                       |
| Build Tool        | Maven                      |
| Version Control   | Git                        |
| CI/CD Tool        | Jenkins                    |
| Containerization  | Docker                     |
| Orchestration     | Kubernetes                 |
| Monitoring        | Prometheus & Grafana       |
| IaC               | Terraform & Ansible        |

## Step 1: Build and Run the Application Using Maven

The backend application is developed using **Java (Spring Boot)** and built using **Maven**.

Prerequisites
- Java 17+
- Maven installed (`mvn -v` to verify)

Build Instructions
1. Open a terminal and navigate to the project root directory.

2. Run the following Maven command to build the application:

   mvn clean install

4. Once the build is successful, start the Spring Boot application:

    mvn spring-boot:run
   
![app- Mvn succeed in backend](https://github.com/user-attachments/assets/fcd8405d-044f-430e-8f40-28bace6fa3ac)

6. By default, the application will be accessible on:

    http://localhost:8082
   
![App running successfully in browser 8082](https://github.com/user-attachments/assets/44cab8cf-4882-40eb-b110-9637f98b0066)

8. Open the URL in a browser. You’ll see a simple HTML-based user interface where users can enter appliance details to estimate their electricity bill.

At this stage, the application is running locally without containers

## 🐳 Step 2: Dockerize and Push the Application

In this step, we will create a Docker image for the application, run it as a container, and push it to Docker Hub.

Prerequisites

- Docker installed (`docker -v` to verify)

- Docker Hub account

Docker Instructions

1. Create a file named `Dockerfile` in the project root with the following content:

   Refer dockerfile file for content

2. Package the application using Maven:

   mvn clean package

3. Build the Docker image:

   docker build -t ebill-estimator:latest .

Explanation:

-t ebill-estimator:latest = Name + version

. = Current directory (Dockerfile location)

![docker built](https://github.com/user-attachments/assets/41674b74-dafa-4236-ac12-a0f9bb85b6e3)

6. Run the Docker container:

    sudo docker run -d -p 8085:8082 --name ebill-estimator ebill-estimator:latest

Explaination:

-d → Run in detached mode (in the background)

-p 8085:8082 → Map host port 8085 to container port 8082

--name ebill-estimator → Name the container

ebill-estimator:latest → Use the image you just built

7. Open your browser and access:

   http://localhost:8085

9. Login to Docker Hub (if not already logged in):

    docker login

11. Tag the image before push to docker hub

     docker tag ebill-estimator:latest lavanya0421/ebill-estimator:latest

13. Push the image to Docker Hub:

    docker push lavanya0421/ebill-estimator:latest
![docker hub browser](https://github.com/user-attachments/assets/ab72a824-9ee9-4c1b-a889-5c8506e5a78a)

Now the image is available on Docker Hub and can be pulled from anywhere.

## 🚀Step 3: Install Jenkins and Create a Freestyle Project

Prerequisites

-Java 17 or higher installed on your system.

-Windows OS (for this guide).

Installation Steps
Jenkins 2.492.2 Installation (on Ubuntu Server)

1. Update and install Java (OpenJDK 17)

    sudo apt update && sudo apt upgrade -y

    sudo apt install openjdk-17-jdk -y (If not installed)

2. Download Jenkins 2.492.2 .deb file manually

    wget https://pkg.jenkins.io/debian-stable/binary/jenkins_2.492.2_all.deb

💡If wget is missing, install it:

    sudo apt install wget -y

3. Install Jenkins from the downloaded .deb file

   sudo dpkg -i jenkins_2.492.2_all.deb

💡If you face missing dependency errors:

    sudo apt --fix-broken install -y

Then re-run:

    sudo dpkg -i jenkins_2.492.2_all.deb

4. Change Jenkins port from 8080 ➜ 9090

   sudo nano /etc/default/jenkins

Find this line:
HTTP_PORT=8080
Change to:
HTTP_PORT=9090

Save and exit (Ctrl + O, then Enter, then Ctrl + X)

5. Change the Listening Port:

    sudo nano /lib/systemd/system/jenkins.service

Modify the line:
Environment="JENKINS_PORT=8080"
to:
Environment="JENKINS_PORT=9090"

6. Reload systemd:
Reload the systemd service manager to apply the changes:

    sudo systemctl daemon-reload

7. Restart Jenkins:
Restart Jenkins for the new configuration to take effect:

    sudo systemctl restart jenkins

8. Verifying Port 9090

Check Listening Ports:

Verify Jenkins is now listening on port 9090:

    sudo netstat -tuln | grep 9090

You should see Jenkins listening on port 9090.

9. Start Jenkins

    sudo systemctl start jenkins

    sudo systemctl enable jenkins

Check status:

    sudo systemctl status jenkins

10. Allow port 9090 (optional if firewall is active)

    sudo ufw allow 9090

11. Access Jenkins
Open browser on your host machine and visit:

     http://localhost:9090

12. Get initial admin password

    sudo cat /var/lib/jenkins/secrets/initialAdminPassword

Copy the password and paste it in the Jenkins UI.

13. Install Suggested Plugins:

Jenkins will prompt you to install plugins. Choose Install suggested plugins for a standard setup.

![Jenkins - installed suggested plugin](https://github.com/user-attachments/assets/08b26eaa-810d-442e-8640-eb78675b886f)

Create Admin User:
Set up your first admin user with a username, password, and email.

Complete Setup:

Once the setup is complete, Jenkins will be ready to use.

Important step in UI

Create a Freestyle Project
Step-by-Step: Create a Freestyle Job in Jenkins

1. Click on “New Item”
2. Enter an Item Name: EB Bill Estimator
3. Select Freestyle project
4. Click OK
![jenkins dashboard after build the job](https://github.com/user-attachments/assets/5efee537-26b7-4d6f-9df9-93086777e9e1)

#Configure the Job
1. Source Code Management (We can do it later also)

Choose Git

Enter your GitHub repo URL:

https://github.com/Lavanya-k-devops/eb-bill-estimator.git

Add credentials if your repo is private.

2. Branch (optional)

Default: */main [Change if needed]

3. Triggers

Tick the GitHub hook trigger for GITScm polling

It is needed when we are going to push thw job automatically github using webhook method.

4. Build Environment (optional)

Check this if needed:

Delete workspace before build starts – to clean old files

 5: Build Steps 

Choose Execute Shell in Add build step and enter as command as your wish

I choosed to dockerize the image automatically

This is my Shell script

-------------------------------------
#!/bin/bash

Navigate to the Electricity Bill Estimator project directory

cd /home/lavanya_k/electricity-bill-estimator/ebill-estimator ||

{

    echo "Failed to cd into project directory"
    
    exit 1

}

Run Maven build

./mvnw clean package ||

{

    echo "Maven build failed"
    
    exit 1

}

Stop and remove any existing Docker container

docker stop ebill-estimator || true

docker rm ebill-estimator || true

Remove old Docker image

docker rmi ebill-estimator:latest || true

Build new Docker image

docker build -t ebill-estimator . || 

{

    echo "Docker build failed"
    
    exit 1

}

Run new container

docker run -d --name ebill-estimator -p 9091:8080 ebill-estimator

------------------------------------

5. Save the changes and back to project

Then click “Build Now” in left side menu.

We can see the build job in bottom of the left side 

![jenkins dashboard after build the job](https://github.com/user-attachments/assets/6936b691-988c-4593-acd0-21f7de52bf10)


We can choose the console output to monitor the real-time logs 

To Test the Setup

Trigger Build:

On your project page, click Build Now to manually trigger a build.

Monitor the build progress and check the console output for any errors.

![jenkins 1st job success](https://github.com/user-attachments/assets/83442a58-81bd-4964-a501-273801058122)

Set Up Webhook: [In future]

In your GitHub repository, go to Settings > Webhooks.

Add a new webhook:

Payload URL: http://<EC2-Public-IP>:9090/github-webhook/

Content type: application/json

Events: Select Just the push event.

This will trigger Jenkins builds on code pushes to your repository.

## Step 4: Kubernetes Setup with Minikube and kubectl (Ubuntu 22.04)

Prerequisites

1. Operating System: Ubuntu 22.04 LTS (64-bit)

2.User Privileges: Sudo access

System Requirements:

1.CPU: 2 cores or more

2.Memory: 2 GB RAM or more

3.Disk Space: 20 GB free space

4.Network: Internet connection

5.Virtualization Support: Ensure virtualization is enabled on your system. Verify with:

grep -E --color 'vmx|svm' /proc/cpuinfo

6.Container or VM Manager: Install one of the following:[Docker or VirtualBox or KVM or Hyper-V or Podman]

Installation Steps

Step by step and install kubectl and minikube safely using official binaries on your Ubuntu 24.04 (noble) system without touching the outdated xenial repo.

1. Install kubectl (Latest Version – Binary Method)

Run the following commands one by one in your terminal:

Download the latest kubectl binary

    curl -LO "https://dl.k8s.io/release/$(curl -Ls https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

Install kubectl binary to /usr/local/bin

    sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

Check version

    kubectl version --client

Expected output should show something like:
    Client Version: v1.29.x

2. Install Minikube (Latest Version)

Download the latest Minikube binary

    curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64

Install it to /usr/local/bin
    
    sudo install minikube-linux-amd64 /usr/local/bin/minikube

Verify installation
    
    minikube version

Expected output:
    minikube version: v1.xx.x

3. Start Minikube with Docker Driver (Recommended)

Only if you have Docker installed:

    minikube start --driver=docker

4. Verify Minikube and kubectl Setup

5. Check Minikube status:

    minikube status

6. Confirm kubectl is configured to use Minikube:

   kubectl config current-context

Expected output:

    minikube

7. View cluster nodes:
    kubectl get nodes

## Deploying to Kubernetes with kubectl apply

Prerequisites

1.Kubernetes Cluster: Ensure Minikube is running.

2.kubectl: Installed and configured to interact with your Minikube cluster.

3.YAML Manifests: Deployment and Service YAML files prepared (e.g., deployment.yaml, service.yaml).

Steps to Apply the YAML:

1. Apply the YAML to your Kubernetes cluster:

    kubectl apply -f ebill-estimator-deployment.yaml

   ![kubernetes terminal result - apply,pods,svc and deployment](https://github.com/user-attachments/assets/14d176d2-a4f6-4599-b35b-7a0777f9e7c4)

2. Verify if the deployment and service were created successfully:
 
    kubectl get deployments

    kubectl get services or kubectl get svc

    kubectl get nodes

    kubectl get pods
	
3. Port Forwarding

Now, proceed with the port forwarding from the service to your local machine.

If you want it to be available locally:

    kubectl port-forward svc/ebill-estimator-service 32000:80

If you want to access it from any IP address (for example, from a VM):

    kubectl port-forward svc/ebill-estimator-service 32000:80 --address=0.0.0.0

here

32000: Local port on your machine.

80: Service port on the Kubernetes service.

4. Access in the Browser

After successful port forwarding, you should be able to access the application on:

    http://localhost:32000
 
   ![kubernetes 32000 in browser](https://github.com/user-attachments/assets/09043254-9962-443c-b5ab-a7a69c6249e3)
We can see our app is live


## Step 5 Installing Helm on Ubuntu

Prerequisites

1.Operating System: Ubuntu (20.04 or later)

2.Kubernetes Cluster: Minikube running locally

3.kubectl: Installed and configured to interact with your Minikube cluster

Step-by-Step: Install & Set Up Helm on Ubuntu

Step 1: Download and Install Helm
Run this on your Ubuntu server (as lavanya_k or jenkins):

curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

This will install the latest stable version of Helm.

Step 2: Verify Helm Installation

helm version

You should see something like:
version.BuildInfo{Version:"v3.x.x", ...}

Step 3: Create Your First Helm Chart

Let's say your app is called ebill-estimator. Run:

helm create ebill-chart

This creates a basic chart folder: Refer ebill-chart folder in https://github.com/Lavanya-k-devops/eb-bill-estimator -> for yaml content 

ebill-chart/
├── charts/             # Sub-charts (dependencies)
├── templates/          # All Kubernetes YAML templates
├── values.yaml         # Default values injected into templates
├── Chart.yaml          # Chart metadata

Step 4: Modify Your Chart

Place your Deployment and Service YAML files in templates/

Parameterize them using Helm templates — use {{ .Values.xxx }} instead of hardcoding values.

You can use your existing ebill-estimator-deployment.yaml as a starting point which we used at kubernetes deployment.

Step 5: Install the Chart to Minikube

helm install ebill-estimator ./ebill-chart

here

ebill-estimator: the name for this deployment

./ebill-chart: path to the Helm chart directory

![Helm - terminal result](https://github.com/user-attachments/assets/9eb11b42-1ff1-4a2d-92fe-4b8e9c9001ed)


To upgrade it later:

helm upgrade ebill-estimator ./ebill-chart

To uninstall:

helm uninstall ebill-estimator

Step 6: Verify Helm Deployment

helm list

kubectl get all

You should see your app running — deployed via Helm!

![helm result in browser](https://github.com/user-attachments/assets/bbc55991-6d67-4f1a-af7a-aa42946b6280)



## Step 6: Monitoring with Prometheus & Grafana on Kubernetes

Prerequisites

1.Kubernetes Cluster: Minikube running locally.

2.Helm: Installed on your system.

Check if Helm is installed:
    helm version

Ensure that your kubectl is configured to point to the correct Kubernetes cluster.

Installation provedure 

## Install Prometheus Using Helm

Step 1: Add Prometheus Helm Chart Repository

Prometheus Helm charts are available in the Prometheus Community repository. To add this repository, run the following command:

    helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
    
    helm repo update

Step 2: Install Prometheus with Helm

To install Prometheus using Helm, use the following command. This will install Prometheus along with additional components like Alertmanager, Node Exporter, and Kube State Metrics.

    helm install prometheus prometheus-community/kube-prometheus-stack

Step 3: Modify Prometheus Port Configuration

By default, Prometheus will run on port 9090, but we need to set it to port 9095 as per your requirement. To do this, you can override the default values during installation:

    helm install prometheus prometheus-community/kube-prometheus-stack \ --set prometheus.server.service.port=9095

Step 4: Verify the Prometheus Installation

After the installation, verify that Prometheus is running correctly by checking the pods:

    kubectl get pods -l release=prometheus

You should see pods like:

prometheus-server-xxxxxx
prometheus-pushgateway-xxxxxx
prometheus-alertmanager-xxxxxx

To Access Prometheus UI

Step 1: Expose Prometheus Using Port Forwarding

To access the Prometheus UI locally on port 9095, use kubectl port-forward:

    kubectl port-forward svc/prometheus-operated 9095:9095 -n  monitoring --address=0.0.0.0

Now, you can access Prometheus UI by navigating to:

    http://localhost:9095

Step 2: Verify Prometheus Targets

In the Prometheus UI, go to Targets to verify that Prometheus is scraping metrics. Open the following URL:

    http://localhost:9095/targets

Here, you should see targets like Node Exporter, Kube State Metrics, and others listed as UP.

![prometheus - target page - my app UP](https://github.com/user-attachments/assets/f122ea46-af80-4531-91bf-efce6903a418)
![prometheus - graph 1](https://github.com/user-attachments/assets/ee886175-cb7b-41c6-9083-a3987fd77891)
![prometheus - graph 2](https://github.com/user-attachments/assets/2da167c9-25b1-4e49-aafa-dcfeddf97741)


## Install Grafana Using Helm

Step 1: Install Grafana

Grafana is also part of the kube-prometheus-stack chart. If Grafana is not installed as part of the Prometheus chart, you can install it separately using the following command:

    helm install grafana prometheus-community/grafana

Step 2: Modify Grafana Port Configuration

To change the Grafana port to 3000 (which is the default port), you can override the values during installation:

    helm install grafana prometheus-community/grafana \
  --set service.port=3000

Step 3: Verify the Grafana Installation

After installation, check the status of Grafana pods:

    kubectl get pods -l release=grafana 
	or
	kubectl get pods -n monitoring

 ![prometheus   Grafana terminal pod list](https://github.com/user-attachments/assets/20a74c02-f297-4ff9-af93-5b3ded3a8dad)

To Access Grafana UI

Step 1: Expose Grafana Using Port Forwarding

You can expose Grafana on port 3000 using port forwarding:

    kubectl port-forward svc/grafana 3000:80 -n monitoring --address=0.0.0.0

Now, access the Grafana UI by navigating to:
    http://localhost:3000

(Default login credentials are: Username: admin, Password: admin)

![grafana - our project mapped correctly](https://github.com/user-attachments/assets/5a6bf384-fbaf-4041-a98f-0bb8a10f490a)

To Configure Grafana Data Source for Prometheus

Step 1: Add Prometheus as Data Source

Once logged into Grafana:

Go to the Configuration (gear icon) in the left menu.

Click on Data Sources and then Add Data Source.

Select Prometheus as the data source type.

In the URL field, enter the Prometheus service URL:
    http://prometheus-operated:9095

![grafana - prometheus added](https://github.com/user-attachments/assets/efb0c5f5-7704-42ce-9706-0fa049a7b6d6)

Click Save & Test to ensure the connection is successful.

To Create Grafana Dashboards

Step 1: Import a Pre-built Dashboard

    Grafana provides many pre-built dashboards for Prometheus metrics. For example, you can import the Node Exporter Full dashboard.

In Grafana:
Click Create -> Import.

Enter the Dashboard ID 1860 (for Node Exporter Full).

Click Load.
Select the Prometheus data source.
Click Import to load the dashboard.

![grafana 4701 panel](https://github.com/user-attachments/assets/64da2a2f-15ef-4f22-90d9-2ff095a42a67)

Step 2: Add Custom Panels

To add custom panels:
Click Create -> Dashboard -> Add Panel.

In the Query section, enter Prometheus queries like:

Memory Usage:
node_memory_Active_bytes / node_memory_MemTotal_bytes * 100

CPU Usage:
(1 - (avg by (instance) (rate(node_cpu_seconds_total{mode="idle"}[5m])))) * 100

Customize the panel’s title, colors, and other settings.

Click Apply to save the panel.

Step 3: Set Up Alerts (Optional)

In each panel, you can set up alerts:

In the panel settings, go to the Alert tab.

Define Alert Conditions (e.g., when memory usage exceeds 80%).

Configure Notification Channels (like email or Slack).

Click Save to apply the alert.

## Step 7: Installing and Configuring Ansible on Ubuntu 22.04

Prerequisites

1.Operating System: Ubuntu Server 20.04 or later (running in VirtualBox)

2.User Privileges: A non-root user with sudo privileges

3.Network Access: Internet connectivity to fetch packages
Installation Steps

1. Update System Packages

Ensure your system packages are up to date:

sudo apt update && sudo apt upgrade -y

2. Install Required Dependencies

Install software properties package to manage PPAs:

sudo apt install software-properties-common -y

3. Add Ansible PPA Repository

Add the official Ansible PPA to get the latest version:

sudo add-apt-repository --yes --update ppa:ansible/ansible

4. Install Ansible

Install Ansible using the APT package manager:

sudo apt install ansible -y

5. Verify Ansible Installation

Confirm the installation by checking the version:

ansible --version

Expected output:

ansible [core 2.16.3]
  config file = None
  configured module search path = ['/home/lavanya_k/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/lib/python3/dist-packages/ansible
  ansible collection location = /home/lavanya_k/.ansible/collections:/usr/share/ansible/collections
  executable location = /usr/bin/ansible
  python version = 3.12.3 (main, Feb  4 2025, 14:48:35) [GCC 13.3.0] (/usr/bin/python3)
  jinja version = 3.1.2
  libyaml = True

Step-by-Step: Ansible Folder Structure & Initial Playbook

Goal of Ansible:

Automate all setup inside our Ubuntu VM (like installing Docker, Jenkins, Java, Node, Maven, Git, etc.)

Our Ansible Folder Structure - Refer Github for yml contents

![ansible folder structure](https://github.com/user-attachments/assets/3da39afe-0e19-4c4c-8e00-107f072bc5d1)

Run the Ansible Playbooks in Order

1. Install Docker -----
ansible-playbook playbooks/setup-docker.yml

2. Install Java ----
ansible-playbook playbooks/setup-java.yml

3. Install Jenkins -----
ansible-playbook playbooks/setup-jenkins.yml

4. Deploy Your App (from DockerHub) -----
ansible-playbook playbooks/deploy-app.yml

Once you run all playbooks successfully and app is up on port 8082 and jenkins is up on port 9090

![ansible - automation on 8082](https://github.com/user-attachments/assets/23e1a58f-21b7-40b4-bdd3-a3a260ab4695)

![ansible - automation on 9090](https://github.com/user-attachments/assets/04d1cad3-a4f9-41e7-a822-5cafe174991c)


## Step 8: Installing Terraform on Ubuntu Server (VirtualBox) and Provisioning AWS EC2 Instance

Prerequisites
1.Operating System: Ubuntu Server 20.04 or later (running in VirtualBox)

2.User Privileges: A non-root user with sudo privileges

3.Network Access: Internet connectivity to fetch packages

4.AWS Account: An active AWS account with access credentials

Let’s kick off with the first step of Terraform setup for provisioning resources in AWS.

1. Install Terraform

If you haven’t already, make sure Terraform is installed on your machine. You can verify installation by running:
     terraform -v

If it’s not installed, you can follow Terraform’s installation guide.

Navigate to Your Project Directory

Ensure you are inside your project directory (where you have your Terraform configuration files).

    cd ~/electricity-bill-estimator/ebill-estimator

Install Terraform (if not already done)

Install Terraform directly inside the server if it's not available yet. Run the following command to install Terraform:

    sudo apt-get update
    
    sudo apt-get install -y wget unzip
    
    wget https://releases.hashicorp.com/terraform/1.5.3/terraform_1.5.3_linux_amd64.zip
    
    unzip terraform_1.5.3_linux_amd64.zip
    
    sudo mv terraform /usr/local/bin/
    
    terraform -v   # To verify installation

Inside the same project directory, Create a Terraform Configuration File

Now, let’s create a tf file for Terraform to define the resources we need (e.g., EC2, security groups, etc.).

Refer Ebill-terraform folder for tf files in github.
Below tf file helps us to define all your resources (like EC2 instances, security groups, etc.).

1.main.tf [Terraform configuration file] ,

2.variables.tf [Optional, for defining variables]

3.outputs.tf [Optional, for outputs]

Key Points to Remember:
Directory: All Terraform files can exist in your project directory, and you can manage your infrastructure from there.
Terraform State: Terraform will generate .tfstate files (the state of your infrastructure) in the same directory, so keep it clean.
Version Control: If you use Git or version control, make sure to add .tfstate and any sensitive files like *.tfvars to .gitignore.

Ubuntu 24.04 (Noble) has no stable awscli v2 package in its default repositories yet.

Let’s fix it by installing AWS CLI v2 manually (official AWS method) — clean, stable, and free-tier safe.

Step-by-Step: Install AWS CLI v2 (Manual Method)

1. Download the installer:

    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    
    Unzip the installer:
    
    unzip awscliv2.zip

Install AWS CLI:

    sudo ./aws/install

Verify installation:

    aws --version

Expected output:
    aws-cli/2.x.x Python/3.x.x Linux/x86_64

2. Configure AWS Credentials

Make sure you have the AWS CLI configured with access to your AWS account. If not, you can configure it using:

    aws configure

Alternatively, you can configure access keys manually and set them as environment variables:

    export AWS_ACCESS_KEY_ID="your-access-key-id"
    
    export AWS_SECRET_ACCESS_KEY="your-secret-access-key"

Step to apply the terraform on AWS EC2

1. To Initialize Terraform

Once the main.tf file is created, initialize the Terraform working directory:

    terraform init

2. Plan the Execution

Check what Terraform will do when it applies the configuration:

    terraform plan

3. Apply the Configuration

If the plan looks good, apply it to provision the EC2 instance:

     terraform apply

Terraform will prompt you to confirm before proceeding.

Once done, Terraform will create the specified resources, and you should see your EC2 instance up and running in the AWS console.

![terraform apply - EC2 instance ](https://github.com/user-attachments/assets/d8f8abfa-40b1-4e4c-aa37-9742270da566)
![terraform apply - Jenkins ](https://github.com/user-attachments/assets/cc4ad9fc-4b0d-4271-9dfb-f85e3e488748)
![terraform apply - UI result](https://github.com/user-attachments/assets/10d623e0-8b99-4b04-9f9d-6e2e376c6abb)

## Step 9 Github Webhook Trigger – Jenkins Job Procedure

1. Pre-requisites

Jenkins is installed and running.

GitHub or GitLab repository available.

Jenkins server is publicly accessible (via IP or domain) OR use ngrok during local testing.

![github created ](https://github.com/user-attachments/assets/6ad6eb0b-a82b-49f2-9d1e-a93b214146ff)

2. Install Essential Plugins in Jenkins

Go to Manage Jenkins → Plugins → Available

Search and install the following:

✅ Git Plugin

✅ GitHub Plugin

✅ GitHub Integration Plugin

✅ GitHub Branch Source Plugin

✅ Pipeline (if using Jenkinsfile)

✅ Generic Webhook Trigger Plugin (optional but useful for flexibility)

After installing, restart Jenkins.

3. Configure GitHub Credentials in Jenkins

Go to Manage Jenkins → Credentials → System → Global Credentials

Click Add Credentials

Type: Username with Password or Personal Access Token

Use your GitHub username and PAT

Give it a recognizable ID (e.g., github-creds)

4. Create a Jenkins Job (Freestyle or Pipeline)

Go to Jenkins Dashboard → New Item

Choose:

Freestyle project – Simple builds

![github ping webhook in jenkins - job details](https://github.com/user-attachments/assets/2a987cb6-98fd-4782-997f-cccc7684807a)

5. Connect GitHub Repository

Under the job configuration:

Scroll to Source Code Management

Select Git

Enter GitHub repository URL

Add credentials you just created (e.g., github-creds)

Set the branch to build (usually main or master)

6. Enable Webhook Trigger

Scroll to Build Triggers section, then:

✅ Check "GitHub hook trigger for GITScm polling"

(Triggers build when webhook is received)

7. Configure Webhook in GitHub

In your GitHub repo:

Go to Settings → Webhooks → Add webhook

Enter:Payload URL: http://<your-ec2-publicip>/github-webhook/

Content type: application/json

Choose events:
Just the push event (default)

Click Add Webhook

Make sure Jenkins server is reachable from GitHub (public URL or tunnel via ngrok)

8. Test the Webhook

Make a change in the GitHub repo (e.g., edit README and push)

Watch Jenkins – the job should trigger automatically

![github ping webhook in jenkins - Systemlog](https://github.com/user-attachments/assets/02fb9c33-2ed1-4b55-80d0-be1d8441c99f)

![github ping webhook in jenkins - console output](https://github.com/user-attachments/assets/b1242157-07f2-4eb0-9f9a-37180b7c759b)

9. Verify Webhook Delivery

In GitHub:
Go to Settings → Webhooks → Click your webhook

See Recent Deliveries
✅ 200 OK = success
![github webhook page](https://github.com/user-attachments/assets/e05d40e6-c7e2-4296-9f2d-ea96f2e5757d)
