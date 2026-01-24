stages {
        // Stage 1: Checkout (Keep this as is)
        stage('Checkout Testing') {
            steps {
                git branch: 'testing', url: "${REPO_URL}"
            }
        }

        // Stage 2: Build (UPDATED)
        stage('Build with Maven') {
            steps {
                // REPLACE 'YOUR_PROJECT_FOLDER_NAME' with the actual folder name!
                // If your pom.xml is in the root, simply verify its name is exactly "pom.xml"
                dir('YOUR_PROJECT_FOLDER_NAME') {
                    bat 'mvn clean package'
                }
            }
        }

        // Stage 3: SonarQube (UPDATED)
        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withSonarQubeEnv('Sonar') {
                        // We must be in the same directory for Sonar to find the compiled classes
                        dir('YOUR_PROJECT_FOLDER_NAME') {
                            bat "\"${scannerHome}\\bin\\sonar-scanner.bat\" -Dsonar.projectKey=Devops_Exp_1 -Dsonar.sources=."
                        }
                    }
                }
            }
        }
        
        // Stage 4: Merge (Keep this as is - git commands work from root usually, 
        // but if you need to add files, you might need to adjust logic later. 
        // For now, let's just get the build passing!)
