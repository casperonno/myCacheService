# MyCacheService

My implementation for a synchronized hash map in Java

## Instructions
  1. Please use the lock_on_the_whole_list branch.
  2. The main branch is an attempt to give a milder condition for the read/write synchronization on the chaining list level. This version sometimes fails on heacy load tests.
  
## Installation

1. Clone the repository: `git clone https://github.com/casperonno/myCacheService.git`
2. Check out the desired branch: `git checkout lock_on_the_whole_list`
3. Build the project: `mvn clean install`
4. Run the project: `java -jar target/myCacheService.jar`


## Supported features:
1. `Dependency Injection` for the cache repo
2. For the cache I used a primitive Java array so I'll have the ability to control the resizing mechanism by myself.
3. I used `junit-jupiter-api for unitests`.

## Credits

I used https://start.spring.io/ to init my project with required dependencies:
  1. Spring Web
  2. Spring Boot DevTools 
