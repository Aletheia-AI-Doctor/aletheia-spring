import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    route("/diagnose", "routes/diagnose.tsx"),
] satisfies RouteConfig;
