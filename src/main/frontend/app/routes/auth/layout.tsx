import {Navigate, Outlet} from "react-router";
import {getFromLocalStorage} from "~/base/helpers";

function AnonymousRoute () {
    const user = getFromLocalStorage('token');
    return user ? <Navigate to="/" replace /> : <Outlet />;
}

export default function LoginLayout() {
    return (
        <div>
            <AnonymousRoute />
        </div>
    );
}
