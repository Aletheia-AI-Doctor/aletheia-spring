import type {Doctor} from "~/features/authentication/authenticationApiSlice";

export default function DoctorMedia({doctor}: { doctor:Doctor  }) {
    return (
        <div className="flex items-center gap-3 mb-2">
            <img
                src={doctor.image}
                alt={doctor.name}
                className="w-8 h-8 rounded-full object-cover"
            />
            <span className="text-sm font-medium text-gray-800">{doctor.name}</span>
        </div>
    );
}