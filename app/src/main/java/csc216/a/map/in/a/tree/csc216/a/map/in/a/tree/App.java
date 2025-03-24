package csc216.a.map.in.a.tree;

public class App {
    static class KeyValuePair implements Comparable<KeyValuePair> {
        private String key;
        private String value;

        KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String getKey() {
            return this.key;
        }

        String getValue() {
            return this.value;
        }

        @Override
        public int compareTo(KeyValuePair o) {
            return this.key.compareTo(o.key);
        }
    }

    static public class SplayTree<T extends Comparable<T>> {
        Node root;

        class Node {
            T value;
            Node left;
            Node right;

            Node(T insertedValue) {
                value = insertedValue;
                left = null;
                right = null;
            }
        }

        Node leftRotation(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        Node rightRotation(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }

        Node splayNode(Node root, T key) {
            if (root == null || root.value.compareTo(key) == 0) {
                return root;
            }

            if (root.value.compareTo(key) > 0) {
                if (root.left == null) {
                    return root;
                }

                if (root.left.value.compareTo(key) > 0) {
                    root.left.left = splayNode(root.left.left, key);
                    root = rightRotation(root);
                } else if (root.left.value.compareTo(key) < 0) {
                    root.left.right = splayNode(root.left.right, key);
                    if (root.left.right != null) {
                        root.left = leftRotation(root.left);
                    }
                }

                if (root.left == null) {
                    return root;
                } else {
                    return rightRotation(root);
                }
            } else {
                if (root.right == null) {
                    return root;
                }

                if (root.right.value.compareTo(key) > 0) {
                    root.right.left = splayNode(root.right.left, key);
                    if (root.right.left != null) {
                        root.right = rightRotation(root.right);
                    }
                } else if (root.right.value.compareTo(key) < 0) {
                    root.right.right = splayNode(root.right.right, key);
                    root = leftRotation(root);
                }

                if (root.right == null) {
                    return root;
                } else {
                    return leftRotation(root);
                }
            }
        }

        Node wrappedInsert(Node root, T value) {
            if (root == null) {
                return new Node(value);
            }

            if (value.compareTo(root.value) < 0) {
                root.left = wrappedInsert(root.left, value);
            } else {
                root.right = wrappedInsert(root.right, value);
            }

            return root;
        }

        public void insert(T value) {
            root = wrappedInsert(root, value);
            root = splayNode(root, value);
        }

        Node wrappedRetrieve(Node root, T value) {
            if (root == null) {
                return null;
            }

            if (root.value.compareTo(value) == 0) {
                return root;
            }

            if (value.compareTo(root.value) < 0) {
                return wrappedRetrieve(root.left, value);
            } else {
                return wrappedRetrieve(root.right, value);
            }
        }

        Node retrieve(T value) {
            Node retrievedNode = wrappedRetrieve(root, value);

            if (retrievedNode != null) {
                root = splayNode(root, retrievedNode.value);
            }

            return retrievedNode;
        }

        Node wrappedRemove(Node root, T value) {
            if (root == null) {
                return null;
            }

            root = splayNode(root, value);

            if (root.value.compareTo(value) != 0) {
                return root;
            }

            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            } else {
                Node temp = root;
                root = splayNode(temp.left, value);

                root.right = temp.right;
            }

            return root;
        }

        static public class labTreeMap {
            private SplayTree<KeyValuePair> tree;

            labTreeMap() {
                tree = new SplayTree<KeyValuePair>();
            }

            void insert(String key, String value) {
                KeyValuePair newPair = new KeyValuePair(key, value);
                tree.insert(newPair);
            }

            String get(String key) {
                KeyValuePair searchPair = new KeyValuePair(key, "");
                SplayTree<KeyValuePair>.Node foundNode = tree.retrieve(searchPair);

                if (foundNode != null) {
                    return foundNode.value.getValue();
                } else {
                    return "";
                }
            }

            public void delete(String key) {
                KeyValuePair searchPair = new KeyValuePair(key, "");
                tree.root = tree.wrappedRemove(tree.root, searchPair);
            }
        }

        public static void main(String[] args) {
            {
                System.out.println("Calling a get() on empty TreeMap:");
                labTreeMap map = new labTreeMap();
                System.out.println(map.get("keyOne"));
            }

            {
                System.out.println("Calling a get() on a One KeyPair TreeMap:");
                labTreeMap map = new labTreeMap();

                map.insert("keyOne", "OnlyValue");

                System.out.println(map.get("keyOne"));

                System.out.println("Calling a get() on the wrong KeyPair TreeMap:");
                System.out.println(map.get("KeySeven"));
                map.delete("keyOne");
                System.out.println("Calling a get() on a deleted KeyPair TreeMap:");
                System.out.println(map.get("keyOne"));
            }
            
            {
                System.out.println("Calling a get() on a Three KeyPairs TreeMap:");
                labTreeMap map = new labTreeMap();

                map.insert("keyOne", "valueOne");
                map.insert("keyTwo", "valueTwo");
                map.insert("keyThree", "valueThree");

                System.out.println(map.get("keyOne"));

                System.out.println(map.get("keyTwo"));

                System.out.println(map.get("keyThree"));

                System.out.println(map.get("keyDoesNotExist"));

                map.delete("keyOne");
                map.delete("keyTwo");
                map.delete("keyThree");

                System.out.println("Calling a get() on a Three deleted KeyPairs TreeMap:");

                System.out.println(map.get("keyOne"));

                System.out.println(map.get("keyTwo"));

                System.out.println(map.get("keyThree"));
            }
        }
    }
}