import { type RouteConfig, index, route, layout } from "@react-router/dev/routes";

export default [
    layout("./layout.tsx", [
        index("routes/home.tsx"),
        route("/diagnose/:model?", "routes/diagnose.tsx"),
        route("/patients", "routes/patient/index.tsx"),
        route("/patients/:id", "routes/patient/show.tsx"),
        route("/profile", "routes/profile/profile.tsx"),
        route("/profile/edit", "routes/profile/edit.tsx"),
        route("/activity", "routes/activity.tsx"),
    ]),

    layout("./routes/auth/layout.tsx", [
        route("login", "./routes/auth/login.tsx"),
        route("registration", "./routes/auth/registration.tsx"),
        route("hospital_subscription", "./routes/auth/hospital-sub.tsx")
    ]),

    layout("./routes/confirmationLayout.tsx",[
        route("/confirm-email/:id","routes/auth/confirm-email.tsx")
    ])
    

] satisfies RouteConfig;

