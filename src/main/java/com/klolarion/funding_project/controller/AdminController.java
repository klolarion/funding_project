package com.klolarion.funding_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminController {
    ResponseEntity<?> closeFunding(@PathVariable Long fundingId);

}
