
import sys

package = repository.read('Applications/argos/argos-test-app/'+sys.argv[1])
control = deployit.prepareControlTask(package, 'collectArgosLink')
taskId = deployit.createControlTask(control)
deployit.startTaskAndWait(taskId)