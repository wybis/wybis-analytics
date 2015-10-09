package io.wybis.wybisanalytics.service

import javax.servlet.http.HttpSession

public interface ConsoleService {

    void reset(HttpSession session);

    void clear(HttpSession session);

    void clearData(HttpSession session);

}
