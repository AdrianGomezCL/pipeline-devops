// ClassPath
package org.cl

// Funcion para validar que el/los stages ingresados esten dentro del listado disponible
// Si no se ingresa ningun stage se ejecutan todos
def validateStage(stage) {

    def stages = params.stage.tokenize(';')
    
    if(stages.contains(stage) || stages.size()==0) return true
    
    return false
}

return this;