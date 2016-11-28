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
- Memory Hierarchy (80% completed)
    - Bundle the files together and add cache leveling
- Tomasulo (20% completed)
- Functional Units (0% completed)


### Getting Started

- Clone the repository  `git clone https://github.com/A-Tokyo/superscalar-microprocessor-stimulator`
- Checkout dev `git checkout dev`
- Create a new branch `git checkout -b branchName` or work on dev (discouraged for conflicts)
- Open eclipse
- Click on File -> Import -> Select General, Existing projects into workspace -> click next
- Select root directory and copy the path to the repository folder or browse for it
- Contribute and send a pull request to dev
- To commit through eclipse, right click on the project, select team, share


## Conventions
- Please follow the following naming conventions http://www.javatpoint.com/java-naming-conventions
