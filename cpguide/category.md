# `Category` description
- Use for `product` classification and filtering
# `Category` relationships
- Each `category` can have zero or more `product`s
- A `category` can be nested to another `category` as a subcategory
- Each `category` has a unique name and optional description
- Each `product` can have one or more `category`
# `Category` attributes
- `id`: integer, auto-generated - The unique identifier for the category
- `name`: string, required, unique - The name of the category
- `description`: string, optional - A brief description of the category
- `parent_category`: reference to another category, optional - The parent category for nested categories
- `created_at`: datetime, auto-generated - The timestamp when the category was created
- `updated_at`: datetime, auto-updated - The timestamp when the category was last updated
# `Category` methods
- `createCategory`(name, description, parent_category): Creates a new category with the specified attributes.'
- `updateCategory`(id, name, description, parent_category): Updates the attributes of an existing category identified by its id.
- `deleteCategory`(id): Deletes the category identified by its id.
- `getCategoryById`(id): Retrieves the category details by its id.
- `listCategories`(): Returns a list of all categories.
- `getProductsByCategory`(category_id): Retrieves all products associated with a specific category.
- `assignProductToCategory`(product_id, category_id): Associates a product with a specific category.
- `removeProductFromCategory`(product_id, category_id): Removes the association of a product from a specific category.
- `getSubCategories`(category_id): Retrieves all sub-categories under a specific category.
- `getParentCategory`(category_id): Retrieves the parent category of a specific category.
- `searchCategories`(search_term): Searches for categories by name or description matching the search term.
- `countProductByCategory`(category_id): Counting a number of product in category.

 
