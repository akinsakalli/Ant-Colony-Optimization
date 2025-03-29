# Ant Colony Optimization

This project is a Java-based application that addresses the **Travelling Salesman Problem** using two distinct approaches: the **Ant Colony Optimization** algorithm and a **Brute-Force** method. This project was developed as part of the coursework for CMPE160: Object Oriented Programming at Bogazici University during the Spring 2024 semester.

The application simulates the scenario of a delivery vehicle that must determine the most efficient route to visit multiple nodes and return to the starting node. It provides a comparative analysis of a heuristic optimization technique (ACO) and an exhaustive search approach (Brute-Force).

## Features

- **Ant Colony Optimization (ACO) Algorithm**  
  Uses a nature-inspired method where multiple agents (called "ants") work together to find short routes. Each ant builds a path by choosing the next node based on:
  - **Pheromone trails**, which represent how successful previous paths were
  - **Distances**, so shorter paths are preferred
  - A balance between following good paths and trying new ones

- **Adjustable Parameters**  
  The behavior of the ACO algorithm can be customized using:
  - `alpha`: controls how much ants follow pheromone trails
  - `beta`: controls how much ants prefer shorter distances
  - `evaporationRate`: how quickly pheromones fade
  - `Q`: how much pheromone ants leave behind
  - Number of ants and number of iterations

- **Brute-Force Approach**  
  Tries every possible route to find the best one. This guarantees the correct solution but is only practical for small numbers of delivery points. It is mainly used for comparison with the ACO results.
  
- **Map Displaying**  
  The nodes and the computed optimal route are displayed to the screen using the StdDraw library to help visualize the result.

- **Clean and Modular Code Structure**  
  The code is organized into separate classes (such as `Ant`, `Node`, and `Edge`) to make it easier to read, understand, and modify.

## Input Format

Each input file contains:

1. An integer `N` — the number of nodes (including the starting node)
2. `N` lines, each representing a node's coordinates in 2D space
3. An `N × N` matrix representing distances between all pairs of nodes
