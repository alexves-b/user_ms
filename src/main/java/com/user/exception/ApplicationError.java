package com.user.exception;

public class ApplicationError {
        private int statusCode;
        private String message;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ApplicationError() {
        }

        public ApplicationError(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
}
