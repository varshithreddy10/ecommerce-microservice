package com.ecom.customerapi.springsecurity.config;



import com.ecom.customerapi.enums.Permission;
import com.ecom.customerapi.enums.Role;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ecom.customerapi.enums.Permission.*;
import static com.ecom.customerapi.enums.Role.*;


@Slf4j
public class PermissionMapping
{
    /*private static final Map<Role, Set<Permission>> map = Map.of(
            USER, Set.of(USER_VIEW, POST_VIEW),
            SELLER, Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE),
            ADMIN, Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE, USER_DELETE, USER_CREATE, POST_DELETE)
    );*/

    private static final Map<Role, Set<Permission>> map = Map.of(

            /* ================= CUSTOMER ================= */
            USER, Set.of(
                    USER_VIEW, USER_UPDATE,
                    ADDRESS_VIEW, ADDRESS_CREATE, ADDRESS_UPDATE, ADDRESS_DELETE,
                    PRODUCT_VIEW, CATEGORY_VIEW,
                    CART_VIEW, CART_ADD_ITEM, CART_REMOVE_ITEM, CART_CLEAR,
                    ORDER_CREATE, ORDER_VIEW, ORDER_CANCEL,
                    PAYMENT_INITIATE, PAYMENT_VIEW
            ),

            /* ================= SELLER ================= */
            SELLER, Set.of(
                    USER_VIEW, USER_UPDATE,
                    ADDRESS_VIEW, ADDRESS_CREATE, ADDRESS_UPDATE, ADDRESS_DELETE,
                    PRODUCT_VIEW, PRODUCT_CREATE, PRODUCT_UPDATE,
                    CATEGORY_VIEW,
                    INVENTORY_VIEW, INVENTORY_UPDATE,
                    ORDER_VIEW
            ),

            /* ================= ADMIN ================= */
            ADMIN, Set.of(
                    USER_VIEW, USER_VIEW_ALL, USER_CREATE, USER_UPDATE, USER_DELETE,
                    ADDRESS_VIEW, ADDRESS_CREATE, ADDRESS_UPDATE, ADDRESS_DELETE,
                    PRODUCT_VIEW, PRODUCT_CREATE, PRODUCT_UPDATE, PRODUCT_DELETE,
                    CATEGORY_VIEW, CATEGORY_CREATE, CATEGORY_UPDATE, CATEGORY_DELETE,
                    CART_VIEW, CART_VIEW_ALL, ORDER_VIEW,
                    ORDER_UPDATE, ORDER_VIEW_ALL, ORDER_CANCEL,
                    INVENTORY_VIEW, INVENTORY_UPDATE,
                    PAYMENT_VIEW

            )
    );

    public static Set<String> getAuthoritiesForRole(Role role)
    {
        log.info("PermissionMapping → resolving permissions for role {}", role);

         Set<String> authorities  =  map.getOrDefault(role, Set.of())
                                        .stream()
                                        .map(Permission::name)
                                        .collect(Collectors.toSet());
        return authorities;
    }

  /*  public static Set<String> getAuthoritiesForRole(Role role)
    {

        Set<String> allpermissions = map.get(role).stream()
                .map(permission -> permission))
                .collect(Collectors.toSet());

        return allpermissions;
    }

    public static Set<String> getAuthoritiesForRole(Role roles)
    {


        Set<String> authorities = new HashSet<>();

        for (Role role : roles) {
            // ROLE authority
            authorities.add("ROLE_" + role.name());

            // Permission authorities
            ROLE_PERMISSION_MAP
                    .getOrDefault(role, Set.of())
                    .forEach(p -> authorities.add(p.name()));
        }

        return authorities;
    }
*/

}
