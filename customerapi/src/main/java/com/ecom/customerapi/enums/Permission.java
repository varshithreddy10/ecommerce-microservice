package com.ecom.customerapi.enums;

public enum Permission
{
    /* ================= USER ================= */
    USER_VIEW, USER_CREATE, USER_UPDATE, USER_DELETE, USER_VIEW_ALL,

    /* ================= ADDRESS ================= */
    ADDRESS_VIEW, ADDRESS_CREATE, ADDRESS_UPDATE, ADDRESS_DELETE,

    /* ================= PRODUCT ================= */
    PRODUCT_VIEW, PRODUCT_CREATE,PRODUCT_UPDATE, PRODUCT_DELETE,

    /* ================= CATEGORY ================= */
    CATEGORY_VIEW, CATEGORY_CREATE, CATEGORY_UPDATE, CATEGORY_DELETE,

    /* ================= ORDER ================= */
    ORDER_VIEW, ORDER_CREATE, ORDER_UPDATE, ORDER_CANCEL, ORDER_VIEW_ALL,

    /* ================= CART ================= */
    CART_VIEW, CART_ADD_ITEM, CART_REMOVE_ITEM, CART_CLEAR, CART_VIEW_ALL,

    /* ================= PAYMENT ================= */
    PAYMENT_INITIATE, PAYMENT_VIEW,

    /* ================= INVENTORY ================= */
    INVENTORY_VIEW, INVENTORY_UPDATE
}

