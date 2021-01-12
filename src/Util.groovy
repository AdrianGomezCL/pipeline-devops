package org.cl

def validateStage(stage) {

    def stages = params.stage.tokenize(';')
    
    if(stages.contains(stage) || stages.size()==0) return true
    
    return false
}

return this
