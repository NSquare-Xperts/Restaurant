package com.nsquare.restaurant.model;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class MyOrderDetails {

    public String order_id;
    public String order_invoice_no;
    public String order_customer_mobile;
    public String order_customer_name;
    public String order_table;
    public String order_bill_amount;
    public String order_discount_amount;
    public String order_tax_amount;
    public String order_total_amount;
    public String order_is_inprocess;
    public String order_is_completed;
    public String order_is_paid;
    public String order_payment_type;
/*
    public MyOrderDetails(String id, String dish_quantity, String dish_price, String dish_total_price) {
        this.id = id;
        this.dish_quantity = dish_quantity;
        this.dish_price = dish_price;
        this.dish_total_price = dish_total_price;
    }*/

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_invoice_no() {
        return order_invoice_no;
    }

    public void setOrder_invoice_no(String order_invoice_no) {
        this.order_invoice_no = order_invoice_no;
    }

    public String getOrder_customer_mobile() {
        return order_customer_mobile;
    }

    public void setOrder_customer_mobile(String order_customer_mobile) {
        this.order_customer_mobile = order_customer_mobile;
    }

    public String getOrder_customer_name() {
        return order_customer_name;
    }

    public void setOrder_customer_name(String order_customer_name) {
        this.order_customer_name = order_customer_name;
    }

    public String getOrder_table() {
        return order_table;
    }

    public void setOrder_table(String order_table) {
        this.order_table = order_table;
    }

    public String getOrder_bill_amount() {
        return order_bill_amount;
    }

    public void setOrder_bill_amount(String order_bill_amount) {
        this.order_bill_amount = order_bill_amount;
    }

    public String getOrder_discount_amount() {
        return order_discount_amount;
    }

    public void setOrder_discount_amount(String order_discount_amount) {
        this.order_discount_amount = order_discount_amount;
    }

    public String getOrder_tax_amount() {
        return order_tax_amount;
    }

    public void setOrder_tax_amount(String order_tax_amount) {
        this.order_tax_amount = order_tax_amount;
    }

    public String getOrder_total_amount() {
        return order_total_amount;
    }

    public void setOrder_total_amount(String order_total_amount) {
        this.order_total_amount = order_total_amount;
    }

    public String getOrder_is_inprocess() {
        return order_is_inprocess;
    }

    public void setOrder_is_inprocess(String order_is_inprocess) {
        this.order_is_inprocess = order_is_inprocess;
    }

    public String getOrder_is_completed() {
        return order_is_completed;
    }

    public void setOrder_is_completed(String order_is_completed) {
        this.order_is_completed = order_is_completed;
    }

    public String getOrder_is_paid() {
        return order_is_paid;
    }

    public void setOrder_is_paid(String order_is_paid) {
        this.order_is_paid = order_is_paid;
    }

    public String getOrder_payment_type() {
        return order_payment_type;
    }

    public void setOrder_payment_type(String order_payment_type) {
        this.order_payment_type = order_payment_type;
    }
}
