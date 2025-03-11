# Test App

### How to run:
* set environment variable `URL=localhost:8081` with URL of another application instance 
* set port `-Dserver.port=8081`
* run app

### Summary:
* The application works on the basis of heart beat.
* When first successful synchronisation using heart beat is reached, instances determine who is the first to step, using double comparison 
* Each beat application check if it should do next step
* I suppose the only way to lose synchronization is connection fail
* I suppose the only one instance can connect to this instance
* If connection is failed, application wait for recover it
* Main game logic is fully covered by unit test.
* It will be nice to cover synchronization service by unit tests too, I can add if needed.