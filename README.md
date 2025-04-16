# Bank-Application

A Spring Boot Banking Application

About

This project is a comprehensive banking system built using Spring Boot. It implements essential banking functionalities with robust JWT-based authentication, secure REST APIs, and layered architecture. The application supports user account management, transactions, and bank statement generation in both view and downloadable PDF formats.

Key Features

1. Authentication & Security
JWT-based login and role-based access control

Custom JwtAuthenticationFilter and JwtTokenProvider

Secure endpoints using Spring Security

2. User & Account Management

Create and manage user accounts

Perform balance checks, update user details, and delete accounts

Credit and debit functionalities

3. Transaction Handling
   
Record and manage account transactions

Generate bank statements based on date ranges

Export statements as downloadable PDF files

4. RESTful Architecture
   
Follows REST best practices

Well-structured controller-service-repository layers

Error handling and custom exception management

Technologies Used

Spring Boot – Backend framework

Java – Core programming language

Spring Security + JWT – Authentication and authorization

Spring Data JPA + Hibernate – ORM and data access

MySQL – Relational database

iText / PDF Libraries – PDF generation for bank statements

Lombok – Boilerplate reduction

Postman – API testing and validation
