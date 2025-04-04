import hudson.console.*

def init(){
    // Prepopulate the array with the default value, which is used if no YAML selected
    yamlsList = ['env-variable']
    def files
    echo "Test"
    dir('profiles') {
       files = findFiles(glob: '*.yaml')
    }
    files.each { file ->
        yamlsList.add(file.name)
    }
    //checkRequiredParams()
    //if(SkipAllStages) {
    //    setBuildTitle("Parameter Initialization")
    //}

    //LinkedHashMap fullYamlPath = [:]
    //LinkedHashMap jobConfig = [:]

}

fullYamlPath = [:]

// Holds the deployment configuration data
jobConfig = [:]



def ReadConfig(){
    // use DeployYaml parameter value, if it's not default env-variable
    if (params.DeployYaml != "env-variable"){
        env.yamlstouse = params.DeployYaml
    }
    //selectStagesToRun()
    yamls = readYaml file: "profiles/"+env.yamlstouse
    addFileToPathMap(env.yamlstouse, "profiles/"+env.yamlstouse)
    addDataToConfig(yamls)
    printJobConfig()
    // save jobConfig as JSON in the ENV variable,
    // to get access from shell scripts (using common.sh get_job_config function)
    env.jobConfig = groovy.json.JsonOutput.toJson(jobConfig);        
    setBuildTitle(env.yamlstouse)
    //storeGitCredentials()

    //addStageToStagesRan("Read Config")
}


def addFileToPathMap(fName, fPath){
    String prefix = 'https://github.com/rcbassil/mypipeline/blob/'
    String branch = scm.branches[0].name
    branch = branch.replace("*/", "").trim() + '/'
    fullYamlPath[fName] = prefix + branch + fPath
}

def addDataToConfig(data){
    data.each { key, value ->
        jobConfig[key] = value
    }
}

def printJobConfig(){
    String outputStr = "Deployment Yaml: ${printYamlLink(env.yamlstouse)}\njobConfig:\n"
    jobConfig.each { key, value ->
        outputStr += "  ${key}: ${printYamlLink(value)}\n"
    }
    println outputStr
}

String getHyperlink(String url, String text) {
    return hudson.console.ModelHyperlinkNote.encodeTo(url, text)
}

def printYamlLink(value){
    if("${value}".endsWith(".yaml")){
        value = getHyperlink(fullYamlPath[value], value)
    }
    return value
}

def setBuildTitle(title){

    // add userName
    //if(!SkipAllStages){
        def userCause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')
        //if(isManuallyStartedBuild()){            
            title += " "+userCause.userName
       // }else{
       //     title += " (timer)"
       // }
    //}
    currentBuild.displayName = "#${BUILD_NUMBER}: " + title
}


return this
