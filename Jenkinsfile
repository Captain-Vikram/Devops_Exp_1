pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/Captain-Vikram/Devops_Exp_1.git'
    }

    stages {
        stage('Checkout Testing') {
            steps {
                git branch: 'testing', url: "${REPO_URL}"
            }
        }

        stage('Compile Java') {
            steps {
                bat 'javac *.java' 
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withSonarQubeEnv('Sonar') {
                        bat "\"${scannerHome}\\bin\\sonar-scanner.bat\" -Dsonar.projectKey=Devops_Exp_1 -Dsonar.sources=."
                    }
                }
            }
        }

        stage('Merge & Push to Main') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-login', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                        bat """
                            rem 1. FORCE CLEANUP: Delete the temporary .scannerwork folder so git doesn't complain
                            git clean -fdx
                            git reset --hard

                            rem 2. Switch to main
                            git checkout main
                            git pull origin main

                            rem 3. Merge testing (If conflict, keep the 'testing' version)
                            git merge -X theirs testing

                            rem 4. Push to GitHub
                            git push https://%GIT_USER%:%GIT_PASS%@github.com/Captain-Vikram/Devops_Exp_1.git main
                        """
                    }
                }
            }
        }
    }
}
