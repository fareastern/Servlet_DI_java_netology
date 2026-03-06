package netology.servlet;

import netology.config.AppConfig;
import netology.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";
    private static final String POSTS_PATH = "/api/posts";
    private static final String POSTS_BY_ID = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(AppConfig.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            final String path = req.getRequestURI();
            final String method = req.getMethod();

            if (METHOD_GET.equals(method) && POSTS_PATH.equals(path)) {
                controller.all(resp);
                return;
            }

            if (METHOD_GET.equals(method) && path.matches(POSTS_BY_ID)) {
                long id = extractId(path);
                controller.getById(id, resp);
                return;
            }

            if (METHOD_POST.equals(method) && POSTS_PATH.equals(path)) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (METHOD_DELETE.equals(method) && path.matches(POSTS_BY_ID)) {
                long id = extractId(path);
                controller.removeById(id, resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Избегаем дублирования методов извлечения в service()
    private long extractId(String path) {
        return Long.parseLong(
                path.substring(path.lastIndexOf("/") + 1)
        );
    }
}

