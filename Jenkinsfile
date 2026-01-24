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

        // --- NEW STAGE: THE GUARD ---
        stage('Quality Gate') {
            steps {
                // This makes Jenkins pause and wait for SonarQube's decision
                // If the Quality Gate is "FAILED", the pipeline STOPS here.
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        // -----------------------------

        stage('Merge & Push to Main') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-login', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                        bat """
                            rem Clean up previous runs
                            git clean -fdx
                            git reset --hard
                            git config --local credential.helper ""

                            rem Prepare merge
                            git checkout main
                            git pull origin main
                            
                            rem Merge
                            git merge -X theirs testing
                            
                            rem Push (Only happens if Quality Gate passed!)
                            git push https://%GIT_USER%:%GIT_PASS%@github.com/Captain-Vikram/Devops_Exp_1.git main
                        """
                    }
                }
            }
        }
    }
}
