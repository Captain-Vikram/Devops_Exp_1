pipeline {
    agent any

    // 1. We tell Jenkins to look for a Maven installation named "Maven"
    tools {
        maven 'Maven' 
    }

    environment {
        REPO_URL = 'https://github.com/Captain-Vikram/Devops_Exp_1.git'
    }

    stages {
        // Stage 1: Check the 'testing' branch
        stage('Checkout Testing') {
            steps {
                git branch: 'testing', url: "${REPO_URL}"
            }
        }

        // Stage 2: Build (Compiles the Java code)
        stage('Build with Maven') {
            steps {
                // Windows command to build
                bat 'mvn clean package' 
            }
        }

        // Stage 3: SonarQube Scan
        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    
                    // 2. We look for the server configuration named "Sonar"
                    withSonarQubeEnv('Sonar') {
                        bat "\"${scannerHome}\\bin\\sonar-scanner.bat\" -Dsonar.projectKey=Devops_Exp_1 -Dsonar.sources=."
                    }
                }
            }
        }

        // Stage 4: Merge to Main (Only runs if Build + Sonar pass)
        stage('Merge & Push to Main') {
            steps {
                script {
                    // CRITICAL: You must have created the 'github-login' credential
                    withCredentials([usernamePassword(credentialsId: 'github-login', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                        bat """
                            git config user.email "jenkins@localhost"
                            git config user.name "Jenkins Bot"
                            
                            git checkout main
                            git pull origin main
                            git merge testing
                            
                            git push https://%GIT_USER%:%GIT_PASS%@github.com/Captain-Vikram/Devops_Exp_1.git main
                        """
                    }
                }
            }
        }
    }
}
