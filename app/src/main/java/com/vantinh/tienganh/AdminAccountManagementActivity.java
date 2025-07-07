package com.vantinh.tienganh;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountManagementActivity extends AppCompatActivity {

    private SearchView svSearchUsers;
    private ListView lvUsers;
    private Button btnAddUser, btnRefresh;
    private List<User> userList;
    private List<User> filteredUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account_management);

        initViews();
        loadUsers();
        setupSearchView();
        setupListView();
        setupClickListeners();
    }

    private void initViews() {
        svSearchUsers = findViewById(R.id.sv_search_users);
        lvUsers = findViewById(R.id.lv_users);
        btnAddUser = findViewById(R.id.btn_add_user);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    private void loadUsers() {
        // TODO: Load users from database
        userList = new ArrayList<>();
        
        // Sample data
        userList.add(new User("1", "John Doe", "john.doe@example.com", "Student", "Active"));
        userList.add(new User("2", "Jane Smith", "jane.smith@example.com", "Teacher", "Active"));
        userList.add(new User("3", "Bob Johnson", "bob.johnson@example.com", "Student", "Inactive"));
        userList.add(new User("4", "Alice Brown", "alice.brown@example.com", "Teacher", "Active"));
        userList.add(new User("5", "Charlie Wilson", "charlie.wilson@example.com", "Admin", "Active"));

        filteredUserList = new ArrayList<>(userList);
    }

    private void setupSearchView() {
        svSearchUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

    private void setupListView() {
        updateListView();
        
        lvUsers.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = filteredUserList.get(position);
            showUserOptionsDialog(selectedUser);
        });
    }

    private void setupClickListeners() {
        btnAddUser.setOnClickListener(v -> {
            // TODO: Open add user dialog or activity
            Toast.makeText(this, "Add user functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        btnRefresh.setOnClickListener(v -> {
            loadUsers();
            updateListView();
            Toast.makeText(this, "User list refreshed", Toast.LENGTH_SHORT).show();
        });
    }

    private void filterUsers(String query) {
        filteredUserList.clear();
        if (query.isEmpty()) {
            filteredUserList.addAll(userList);
        } else {
            for (User user : userList) {
                if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                    user.getRole().toLowerCase().contains(query.toLowerCase())) {
                    filteredUserList.add(user);
                }
            }
        }
        updateListView();
    }

    private void updateListView() {
        List<String> userStrings = new ArrayList<>();
        for (User user : filteredUserList) {
            userStrings.add(user.getName() + "\n" + 
                          user.getEmail() + " | " + user.getRole() + " | " + user.getStatus());
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, userStrings);
        lvUsers.setAdapter(adapter);
    }

    private void showUserOptionsDialog(User user) {
        String[] options = {"View Details", "Edit User", "Change Status", "Delete User"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User: " + user.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // View Details
                            showUserDetails(user);
                            break;
                        case 1: // Edit User
                            editUser(user);
                            break;
                        case 2: // Change Status
                            changeUserStatus(user);
                            break;
                        case 3: // Delete User
                            deleteUser(user);
                            break;
                    }
                });
        builder.show();
    }

    private void showUserDetails(User user) {
        String details = "Name: " + user.getName() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Role: " + user.getRole() + "\n" +
                        "Status: " + user.getStatus();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Details")
                .setMessage(details)
                .setPositiveButton("OK", null);
        builder.show();
    }

    private void editUser(User user) {
        // TODO: Open edit user dialog or activity
        Toast.makeText(this, "Edit user: " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    private void changeUserStatus(User user) {
        String newStatus = user.getStatus().equals("Active") ? "Inactive" : "Active";
        user.setStatus(newStatus);
        updateListView();
        Toast.makeText(this, "User status changed to: " + newStatus, Toast.LENGTH_SHORT).show();
    }

    private void deleteUser(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    userList.remove(user);
                    filteredUserList.remove(user);
                    updateListView();
                    Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null);
        builder.show();
    }

    // Inner class for User model
    private static class User {
        private String id;
        private String name;
        private String email;
        private String role;
        private String status;

        public User(String id, String name, String email, String role, String status) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.status = status;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}