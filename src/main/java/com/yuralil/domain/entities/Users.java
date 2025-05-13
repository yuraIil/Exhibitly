package com.yuralil.domain.entities;

/**
 * Представляє користувача системи.
 * Містить інформацію про логін, пароль та роль (наприклад: visitor, scientific_staff, administrator).
 */
public class Users {
    private int id;
    private String username;
    private String password;
    private String role;

    /**
     * Порожній конструктор.
     */
    public Users() {
    }

    /**
     * Повний конструктор з ID.
     *
     * @param id       ідентифікатор користувача
     * @param username логін користувача
     * @param password пароль користувача
     * @param role     роль користувача в системі
     */
    public Users(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Конструктор без ID (наприклад, для створення нового користувача).
     *
     * @param username логін користувача
     * @param password пароль користувача
     * @param role     роль користувача
     */
    public Users(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Повертає ID користувача.
     *
     * @return ідентифікатор
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ID користувача.
     *
     * @param id новий ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Повертає логін користувача.
     *
     * @return імʼя користувача
     */
    public String getUsername() {
        return username;
    }

    /**
     * Встановлює логін користувача.
     *
     * @param username новий логін
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Повертає пароль користувача.
     *
     * @return пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * Встановлює пароль користувача.
     *
     * @param password новий пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Повертає роль користувача (visitor, scientific_staff, administrator тощо).
     *
     * @return роль користувача
     */
    public String getRole() {
        return role;
    }

    /**
     * Встановлює роль користувача.
     *
     * @param role нова роль
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Повертає текстове представлення користувача (без пароля).
     *
     * @return рядок з ID, логіном і роллю
     */
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
