package org.billing.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.billing.domain.SubscriberDB;
import org.billing.repo.JdbcSubscriberRepository;
import org.billing.service.SubscriberService;

import java.io.IOException;
import java.util.Optional;


public class SubscriberServlet extends HttpServlet {
    private SubscriberService service;


    @Override
    public void init() {
        JdbcSubscriberRepository repo = new JdbcSubscriberRepository();
        service = new SubscriberService(repo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        resp.setContentType("text/plain");

        if (idParam == null) {
            // Нет id → выводим всех подписчиков
            var allSubscribers = service.findAll();
            if (allSubscribers.isEmpty()) {
                resp.getWriter().write("Нет пользователей в базе.");
                return;
            }

            StringBuilder output = new StringBuilder();
            output.append(String.format("%-5s %-15s %-25s %-10s %-7s%n", "ID", "Username", "Email", "Role", "Blocked"));
            output.append("---------------------------------------------------------------\n");

            for (SubscriberDB s : allSubscribers) {
                output.append(String.format(
                        "%-5d %-15s %-25s %-10s %-7s%n",
                        s.getId(),
                        s.getUsername(),
                        s.getEmail(),
                        s.getRole(),
                        s.isBlocked()
                ));
            }

            resp.getWriter().write(output.toString());
            return;
        }

        // Если id передан → ищем конкретного подписчика
        try {
            int id = Integer.parseInt(idParam);
            Optional<SubscriberDB> sub = service.findById(id);
            if (sub.isPresent()) {
                SubscriberDB s = sub.get();
                String single = String.format(
                        "ID: %d%nUsername: %s%nEmail: %s%nRole: %s%nBlocked: %s%n",
                        s.getId(), s.getUsername(), s.getEmail(), s.getRole(), s.isBlocked()
                );
                resp.getWriter().write(single);
            } else {
                resp.getWriter().write("Subscriber not found.");
            }
        } catch (NumberFormatException e) {
            resp.getWriter().write("Invalid id format.");
        }
    }





}
