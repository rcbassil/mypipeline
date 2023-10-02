node {
	stage("Read Config") {
		script {
	
			def configVal = readYaml file: "config.yaml"
		    echo "configVal: " + configVal
    
            echo configVal['repo']
            env.REPO = configVal['repo']
		}
	}
}

pipeline {
    agent {
        docker {
            image 'rcbassil/react-container'
            args '-p 3000:3000'
        }
    }
    environment {
        CI = 'true'
    }
    stages {
        stage('Setup parameters') {
            steps {
                script { 
                    properties([
                        parameters([
                            choice(
                                choices: ['ONE', 'TWO'], 
                                name: 'PARAMETER_01'
                            ),
                            booleanParam(
                                defaultValue: true, 
                                description: '', 
                                name: 'BOOLEAN'
                            ),
                            text(
                                defaultValue: '''
                                this is a multi-line 
                                string parameter example
                                ''', 
                                 name: 'MULTI-LINE-STRING'
                            ),
                            string(
                                defaultValue: 'scriptcrunch', 
                                name: 'STRING-PARAMETER', 
                                trim: true
                            )
                        ])
                    ])
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Build'
                echo "${REPO}"
                sh 'rm -rf react && git clone https://github.com/rcbassil/react && cd react/expensesapp && npm install'
            }
        }
        stage('Test') {
            steps {
                echo 'Test'
                sh 'cp ./scripts/test.sh react/expensesapp/'
                sh 'cd react/expensesapp && chmod +x test.sh && ./test.sh'
            }
        }
        stage('Deliver') {
            steps {
                echo 'Deliver'
                sh 'cp ./scripts/deliver.sh react/expensesapp/'
                sh 'cp ./scripts/kill.sh react/expensesapp/'
                sh 'cd react/expensesapp && chmod +x deliver.sh && ./deliver.sh'
                input message: 'Finished using the web site? (Click "Proceed" to continue)'
                sh 'cd react/expensesapp && chmod +x kill.sh && ./kill.sh'
            }
        }
    }
}