CREATE TABLE Admin (
  admin_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL
);

CREATE TABLE Friend (
  friend_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  password VARCHAR(50) NOT NULL
);

CREATE TABLE FriendRequest (
  request_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  status VARCHAR(20) NOT NULL,  
  FOREIGN KEY (sender_id) REFERENCES Friend(friend_id) ON DELETE CASCADE,
  FOREIGN KEY (receiver_id) REFERENCES Friend(friend_id) ON DELETE CASCADE
);

CREATE TABLE Wishlist (
  wishlist_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  friend_id INT NOT NULL,
  wishlist_name VARCHAR(100) NOT NULL,
  wishlist_description TEXT,  
  FOREIGN KEY (friend_id) REFERENCES Friend(friend_id) ON DELETE CASCADE
);

CREATE TABLE Item (
  item_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_name VARCHAR(100) NOT NULL,
  item_description TEXT,
  cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE Gift (
  gift_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_id INT NOT NULL,
  wishlist_id INT NOT NULL,
  FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE CASCADE,
  FOREIGN KEY (wishlist_id) REFERENCES Wishlist(wishlist_id) ON DELETE CASCADE
);

CREATE TABLE Contribution (
  contribution_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  
  gift_id INT NOT NULL,
  contibutor_id INT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  FOREIGN KEY (gift_id) REFERENCES Gift(gift_id) ON DELETE CASCADE,
  FOREIGN KEY (contibutor_id) REFERENCES Friend(friend_id) ON DELETE CASCADE
);
