package com.cybertitans.CyberTitans.service;

import java.io.IOException;

public interface TemplateHelper {
    String compile(String name,Object context) throws IOException;
}
