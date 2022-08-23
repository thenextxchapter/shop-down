<h1 style="font-size: 40px" align="center">
<img src="logo-medium.png" width="400">
    <br>
</h1>

🛍 &nbsp; An eCommerce web application that allows users to buy and sell any kind of product.

## Features/Functions

Basics (Administrator Layout, Customer facing Layout, Orders, Billing & Payment)

### Shop Down Administrator Management
- **There are five (5) types of Users**
    - **Admin**: Manages everything
    - **Sales Person**: Manages product price, customers, shipping, orders and sales report
    - **Editor**: Manages categories, brands, products, articles and menus
    - **Shipper**: View products, view orders, and update order status
    - **Assistant**: Manages questions and reviews
 

- **User Module**
  - Create | edit | view | delete new and existing users
  - Export to PDF | CSV | Excel files
  - Assign roles (could be more than one to users)
  - Search for users using keywords



- **Category Module**
<br>
Categories are group entities that a product can belong to e.g books, clothing, electronics, etc.
  - Create | edit | view | delete new and existing categories
  - Export to CSV file
  - Category module uses a hierarchical structure
  - Search for categories using keywords


- **Brand Module**
  - Create | edit | view | delete new and existing brands
  - Search for brands using keywords
  - Brands can have products under more than one category


- **Product Module**
  - Create | edit | view | delete new and existing products
  - Search for products using keywords
  - A product can belong to just one brand and you can select just one category under the selected brand
  - You can add descriptions about the product
  - You can add multiple images for the project
  - You can also add shipping details in terms of packaging dimensions which will be used to calculate the shipping cost


### Additional Stuff
**The Application is still a work in progress so as I add more features I'll keep updating this README file**
- Login and User Authentication
- View account profile and make changes


## Technologies
This software uses the following technologies:
* Frontend (HTML | CSS | Thymeleaf | JQuery | Bootstrap)
* Backend (Java | Spring Boot | Spring Security | Maven | Tomcat | JUnit)
* Database (PostgreSQL)
