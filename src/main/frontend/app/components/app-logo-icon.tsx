import type {DetailedHTMLProps, ImgHTMLAttributes} from 'react';
import logo from "app/images/logo_aletheia.jpg";

export default function AppLogoIcon(props: DetailedHTMLProps<ImgHTMLAttributes<HTMLImageElement>, HTMLImageElement>) {
    return (
        <img
            {...props}
            className={"rounded-full " + props.className}
            alt="Alethia's Logo"
            src={logo}
        />
    );
}
