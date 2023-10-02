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
        stage('Build') {
            steps {
                echo 'Build'
                sh 'cd ~/ && git clone https://github.com/rcbassil/react && cd react/expensesapp && npm install'
            }
        }
        stage('Test') {
            steps {
                echo 'Test'
                sh 'cp ./scripts/test.sh ~/react/expensesapp/'
                sh 'cd ~/react/expensesapp && ./test.sh'
            }
        }
        stage('Deliver') {
            steps {
                echo 'Deliver'
                sh 'cp ./scripts/deliver.sh ~/react/expensesapp/'
                sh 'cp ./scripts/kill.sh ~/react/expensesapp/'
                sh 'cd ~/react/expensesapp && ./deliver.sh'
                input message: 'Finished using the web site? (Click "Proceed" to continue)'
                sh 'cd ~/react/expensesapp && ./kill.sh'
            }
        }
    }
}