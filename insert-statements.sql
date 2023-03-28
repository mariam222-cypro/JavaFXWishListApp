INSERT INTO Admin (username, password) VALUES ('admin1', 'password1');
INSERT INTO Admin (username, password) VALUES ('admin2', 'password2');
INSERT INTO Admin (username, password) VALUES ('admin3', 'password3');
INSERT INTO Admin (username, password) VALUES ('admin4', 'password4');

INSERT INTO Friend (name, password) VALUES ('Friend1', 'password1');
INSERT INTO Friend (name, password) VALUES ('Friend2', 'password2');
INSERT INTO Friend (name, password) VALUES ('Friend3', 'password3');
INSERT INTO Friend (name, password) VALUES ('Friend4', 'password4');
INSERT INTO Friend (name, password) VALUES ('Friend5', 'password5');
INSERT INTO Friend (name, password) VALUES ('Friend6', 'password6');

INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (1, 2, 'SENT');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (2, 1, 'CONFIRMED');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (3, 1, 'SENT');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (1, 3, 'CONFIRMED');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (4, 1, 'SENT');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (2, 4, 'CONFIRMED');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (5, 1, 'SENT');
INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (6, 3, 'CONFIRMED');

INSERT INTO Wishlist (friend_id, wishlist_name, wishlist_description) VALUES (1, 'Birthday Wishlist', 'My birthday is coming up and these are some things I would like.');
INSERT INTO Wishlist (friend_id, wishlist_name, wishlist_description) VALUES (2, 'Christmas Wishlist', 'I love Christmas and here are some things I would like.');
INSERT INTO Wishlist (friend_id, wishlist_name, wishlist_description) VALUES (1, 'Christmas List', 'I am hoping for some new winter clothes.');
INSERT INTO Wishlist (friend_id, wishlist_name, wishlist_description) VALUES (4, 'Summer List', 'I want to get some new outdoor gear.');

INSERT INTO Item (item_name, item_description, cost) VALUES ('New Headphones', 'Over-ear headphones with noise-cancellation', 100.00);
INSERT INTO Item (item_name, item_description, cost) VALUES ('Smartwatch', 'A fitness tracker and smartwatch in one', 200.00);
INSERT INTO Item (item_name, item_description, cost) VALUES ('Gaming Mouse', 'A high-performance gaming mouse with customizable buttons', 50.00);
INSERT INTO Item (item_name, item_description, cost) VALUES ('Winter Coat', 'A warm winter coat', 150.00);
INSERT INTO Item (item_name, item_description, cost) VALUES ('Ski Gloves', 'Gloves for skiing and other outdoor activities', 50.00);
INSERT INTO Item (item_name, item_description, cost) VALUES ('Camping Tent', 'A tent for camping trips', 200.00);

INSERT INTO Gift (item_id, wishlist_id) VALUES (1, 1);
INSERT INTO Gift (item_id, wishlist_id) VALUES (2, 1);
INSERT INTO Gift (item_id, wishlist_id) VALUES (3, 2);
INSERT INTO Gift (item_id, wishlist_id) VALUES (1, 1);
INSERT INTO Gift (item_id, wishlist_id) VALUES (2, 1);
INSERT INTO Gift (item_id, wishlist_id) VALUES (3, 4);


INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (1, 2, 50.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (1, 3, 25.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (2, 1, 100.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (3, 2, 25.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (3, 3, 50.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (1, 2, 75.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (1, 3, 25.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (2, 1, 150.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (3, 4, 50.00);
INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (3, 5, 100.00);