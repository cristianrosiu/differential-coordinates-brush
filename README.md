# Mesh Deformation Tool

This project provides a mesh deformation tool for 3D models, implemented in Java. It allows users to apply various geometric transformations (scaling, rotation, translation) to a selected portion of a mesh, while maintaining the mesh's continuity.

## Table of Contents

1. [Features](#features)
2. [Implementation](#implementation)
    1. [Differential Coordinates](#differential-coordinates)
    2. [Shape Editing using Differential Coordinates](#shape-editing-using-differential-coordinates)
3. [Usage](#usage)
4. [Tests](#tests)
5. [Examples](#examples)
6. [Discussion](#discussion)

## Features

- Supports various geometric transformations: scaling, rotation, translation.
- Maintains mesh continuity during transformations.
- Custom data structure for storing computed matrices.

## Implementation

### Differential Coordinates

The first task in this project was to compute matrix S containing gradients of triangles in a mesh. This was achieved by following the steps presented in the tutorial, along with the information from the slides. The algorithm computes gradient matrix G of each triangle and then uses G to compute the final matrix S.

### Shape Editing using Differential Coordinates

The second task involved implementing shape editing by using the differential coordinates calculated in the first task. The algorithm generates the correct embeddings vx, vy, vz for each vertex, calculates their respective gradients, modifies the gradients using a transformation matrix A, and solves the system S * v˜ = G^T * Mv * g˜ for new vertex positions.

## Usage

To apply a brush effect using a transformation matrix A, call the function `applyBrushes(PdMatrix A)`. This function calculates the necessary gradients, updates the mesh vertices, and shifts the barycenter to its previous location.

## Tests

Unit tests have been created to verify the results of the implemented methods with manually calculated results. These tests can be found in `DifferentialCoordinatesTest`. All tests have been passed successfully.

## Examples

Some examples of the mesh deformation tool in action can be found in the report, showcasing experiments on a rabbit head mesh and a cylinder mesh. The tool performs well on all input matrices, including scaling, rotation, and translation.

## Discussion

The Geometric Laplace coordinates method was chosen for this project due to its ability to account for irregularities in the triangles' neighborhood. However, the current implementation is quite slow, taking over 10 minutes to calculate for a mesh with more than 40,000 triangles. With more time, the code could be optimized, and approximation algorithms could be used to speed up computation for larger meshes.
