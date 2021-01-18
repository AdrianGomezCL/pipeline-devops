// Classpath
package org.cl

def checkIfBranchExists(String branch) {

  def var_outout = sh (script: "git ls-remote --heads origin ${branch}", returnStdout: true)

  if (var_outout?.trim()) {
  	return true
  }
  else {
  	return false
  }

}

def deleteBranch(String branch) {
	sh 'git push origin --delete ' + branch
	sh 'git branch -d ' + branch
}


def createBranch(String origin, String newBranch) {
	println "DEBUG: origin=" + origin
	println "DEBUG: newBranch=" + newBranch 
 
	sh 'git pull'
	sh 'git checkout ' + origin
	sh 'git checkout -b ' + newBranch
	sh 'git push origin ' + newBranch
	
	sh 'git checkout ' + origin
}