import hudson.console.*

def init(){
    // Prepopulate the array with the default value, which is used if no YAML selected
    yamlsList = ['env-variable']
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
}

return this