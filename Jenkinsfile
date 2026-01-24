pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Captain-Vikram/Devops_Exp_1.git'
    }

    stages {
        // Stage 1: Checkout
        stage('Checkout Testing') {
            steps {
                git branch: 'testing', url: "${REPO_URL}"
            }
        }

        // Stage 2: Simple Build (No Maven needed)
        stage('Compile Java') {
            steps {
                // This just checks if the code has syntax errors
                // Replace 'App.java' with your actual file name!
                bat 'javac *.java' 
            }
        }

        // Stage 3: SonarQube (Simplified)
        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withSonarQubeEnv('Sonar') {
                        // Scan everything in the current folder
                        bat "\"${scannerHome}\\bin\\sonar-scanner.bat\" -Dsonar.projectKey=Devops_Exp_1 -Dsonar.sources=."
                    }
                }
            }
        }

        // Stage 4: Merge & Push
        stage('Merge & Push to Main') {
            steps {
                script {
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
