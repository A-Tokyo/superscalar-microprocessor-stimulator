# superscalar microprocessor stimulator

## Announcments
###  Current Deadline: 28/11 11:59 PM
Please push all commits to the dev branch, NOT the master branch. The master branch should be only left for completely stable commits, and managed via GitHub. (i.e. through pull requests) of course create a branch with your new feature first.

## Description
The goal of this project is to implement an architectural simulator capable of assessing the performance of a simplified superscalar out-‐‐of-‐‐order 16-‐‐bit RISC
processor that uses Tomasulo’s algorithm with speculation taking into account the effect of the cache organization.


## Contribution
- Tomasulo
    - Tomasulo code exists in the package tomasulo with the class Tomasulo as the bundler
- Functional Units
    - Functional units should be in the package functionalUnits
- Memory Hierarchy
    - Memory Hierarchy code exists in the package memoryHierarchy with the class MemoryHierarchy as the bundler
- Utilitis
    - If you want to add any general function such as mathematical log, or printing a 2D array add them to the Utils class and     import it where you need it.


### TODO
- Memory Hierarchy (100% completed)
    - Bundle the files together and add cache leveling
- Tomasulo (70% completed)
- Functional Units (90% completed)
- Assembler (100% completed)


### Getting Started

- Clone the repository  `git clone https://github.com/A-Tokyo/superscalar-microprocessor-stimulator`
- Checkout dev `git checkout dev`
- Create a new branch `git checkout -b branchName` or work on dev (discouraged for conflicts)
- Open eclipse
- Click on File -> Import -> Select General, Existing projects into workspace -> click next
- Select root directory and copy the path to the repository folder or browse for it
- Contribute and send a pull request to dev
- To commit through eclipse, right click on the project, select team, share

## Run the stimulation
Run to main/Main.java, and input the file name of the program (i.e: programs/program.txt).

## Input syntax
The input file should look as follows:
```
// This is the template of a stimulator input
// Your comments should be prefixed with // and be on a standalone line
// The assembler is case insensitive and space insensitive regarding the arguments
// The key word must be followed with a space, ie JMP reg0,  reg1
// For stimulator initialization inputs, the JSONS are case sensitive for the key as normal
// The normal key: value format however is case insensitive and space insensitive
//
//Initialize memory hierarchy here as follows, must start with a header Memory Hierarchy
Memory  Hierarchy
  number of cache levels: 3
  {   S : 128,  L: 12 , M: 4, writePolicyHit: write through, writePolicyMiss: writeBack, cycles: 8 }
  {   S : 256,  L: 32 , M: 8, writePolicyHit: write through, writePolicyMiss: writeBack, cycles:  4 }
  {   S : 512,  L: 32 , M: 8, writePolicyHit: write through, writePolicyMiss: writeBack, cycles: 16 }
 memory cycles: 12
//
//Initialize hardware organization here as follows
Hardware Organization
  pipeline width : 4
  instruction buffer size: 5
rob size: 6
//
// Functional Units Information goes here
// THIS SHOULD BE A VALID JSON, order doesn't matter as long as you follow the key format
  {add: {addRS: 1,addCycles: 2}, mul: {mulRS: 3, mulCycles: 4}, lw:{lwRS: 5, lwCycles: 5}, lw:{lwRS: 4, lwCycles: 3}, jalr:{jalrRS: 2, jalrCycles: 1}}
//
// Your assembly code goes here it must start with "Assembly" and end with "endAssembly"
// start your code with .org "number which is the memory index"
Assembly
  .org 100
  lw reg2,reg1,8
  sw  reg2, reg1,8
  @label
  add reg2, reg1, reg7
  SUB reg2, reg3 , reg4
  JmP reg5, 7
  // comment
endAssembly
//
//Write your program data here if any must start with the header "program data"
// The data format must be in address:value
program data
  100 : 0101010101010101
  101 : 0101010101010101
end data
// END OF FILE

```

## Conventions
- Please follow the following naming conventions http://www.javatpoint.com/java-naming-conventions
