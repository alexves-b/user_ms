package com.user.dto.account;
public enum StatusCodeType {
 FRIEND("FRIEND"),
 REQUEST_TO("REQUEST_TO"),
 REQUEST_FROM("REQUEST_FROM"),
 BLOCKED("BLOCKED"),
 DECLINED("DECLINED"),
 SUBSCRIBED("SUBSCRIBED"),
 NONE("NONE"),
 WATCHING("WATCHING"),
 REJECTING("REJECTING"),
 RECOMMENDATION("RECOMMENDATION");
 private final String type;
 StatusCodeType(String type) {
  this.type = type;
 }
}
