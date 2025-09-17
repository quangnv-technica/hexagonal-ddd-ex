# `Store` description
- A `Store` is an entity that sells `product`s to customers. 
# `Store` relationships
- Each `store` can have zero or more `product`s.
- Each single `product` belongs to only one `store`.
# `Store` attributes
- `id`: integer, auto-generated - The unique identifier for the store
- `name`: string, required, unique - The name of the store
- `description`: string, optional - A brief description of the store
- `location`: string, optional - The physical location or address of the store
- `created_at`: datetime, auto-generated - The timestamp when the store was created
- `updated_at`: datetime, auto-updated - The timestamp when the store was last updated
# `Store` methods
- `createStore`(name, description, location): Creates a new store with the specified attributes.
- `updateStore`(id, name, description, location): Updates the attributes of an existing store identified by its id.
- `deleteStore`(id): Deletes the store identified by its id.
- `getStoreById`(id): Retrieves the store details by its id.
- `listStores`(): Returns a list of all stores.
- `getProductsByStore`(store_id): Retrieves all products associated with a specific store
- `searchStores`(search_term): Searches for stores by name or description matching the search term.
- `countProductsByStore`(store_id): Counting a number of product in store.
- `getStoreLocation`(store_id): Retrieves the location of a specific store.
- `updateStoreLocation`(store_id, location): Updates the location of a specific store.
- `getStoreByProduct`(product_id): Retrieves the store associated with a specific product.
- `listStoresWithProductCount`(): Returns a list of all stores along with the count of products they have.
- `getTopStoresByProductCount`(limit): Retrieves the top stores based on the number of products they have, limited by the specified number.
- `getRecentlyUpdatedStores`(limit): Retrieves stores that have been recently updated, limited
by the specified number.
- `getStoreCreationDate`(store_id): Retrieves the creation date of a specific store.
- `getStoreUpdateDate`(store_id): Retrieves the last update date of a specific
store.
- `getStoresByLocation`(location): Retrieves all stores located in a specific location.
- `getAllStoreLocations`(): Retrieves a list of all unique store locations.
- `countStoresByLocation`(location): Counting a number of stores in a specific location.
- `getStoreDetailsWithProducts`(store_id): Retrieves detailed information about a specific store along with its associated products.
- `getStoresByProductCategory`(category_id): Retrieves all stores that have products in a specific category.


