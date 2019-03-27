# Field Validation

<a name="creating-a-profile"></a>
## Creating a Profile

**First Name(s)** *required*  
Max Characters: 100   
Allowed Characters: letters, hyphens, apostrophes and spaces  

**Middle Name(s)**  
Max Characters: 100   
Allowed Characters: letters, hyphens, apostrophes and spaces   

**Last Name(s)** *required*  
Max Characters: 100   
Allowed Characters: letters, hyphens, apostrophes and spaces

<a name="username"></a>
**Username** *required*  
Unique to database  
Min Characters: 3  
Max Characters: 15   

<a name="password"></a>
**Password** *required*  
Min Characters: 5  
Max Characters: 15  
Must contain two different character types of:
- Lower Case
- Upper Case
- Number
- Special Characters

**Retype Password** *required*  
Same as *Password*

**Date of Birth** *required*  
Restricted to format in field  

**Gender** *required*  
Restricted to options

**Nationality** *required*  
Restricted to options  
Must select at least one

**Passport Country** *required*  
Restricted to options  
Must select at least one

**Traveller Type** *required*  
Restricted to options  
Must have at least one Traveller Type selected

## Logging In

**Username** *required*  
Min Characters: 5  

**Password** *required*  
Min Characters: 6  

## Editing a Profile

Same fields as *Creating a Profile*.

## Creating a Destination

**Name** *required*  
Cannot exist in the database already.

**Type** *required*  
Dropdown box

**Discrict** *required*  

**Latitude** *required*  
Must be a number

**Longitude** *required*  
Must be a number

**Country** *required*  
