import type {DetailedHTMLProps, ImgHTMLAttributes} from 'react';
import logo from "app/images/newest version.jpg";

export default function AppLogoIcon(props: DetailedHTMLProps<ImgHTMLAttributes<HTMLImageElement>, HTMLImageElement>) {
    return (
        <img
            {...props}
            className={"rounded-md " + props.className}
            alt="Alethia's Logo2"
            src={logo}
        />
    );
}
