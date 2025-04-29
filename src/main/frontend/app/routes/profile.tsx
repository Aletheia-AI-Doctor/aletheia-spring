import { useGetDoctorAttributesQuery } from "~/features/doctor/doctorApiSlice";

export default function DrProfilePage() {
    const {
        data: doctor,
        isLoading,
        isError,
        error
    } = useGetDoctorAttributesQuery();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError) {
        // Handle error response properly
        const errorData = error as { data?: { title?: string, detail?: string } };
        return (
            <div className="text-red-500">
                <h3>Error loading profile</h3>
                <p>{errorData.data?.title || 'Unknown error'}</p>
                {errorData.data?.detail && <p>{errorData.data.detail}</p>}
            </div>
        );
    }

    if (!doctor) {
        return <div>No doctor data found</div>;
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Doctor Profile</h1>
            <div className="space-y-2">
                <p><strong>Name:</strong> {doctor.name}</p>
                <p><strong>Username:</strong> {doctor.username}</p>
                <p><strong>Email:</strong> {doctor.email}</p>
                <p><strong>Specialty:</strong> {doctor.specialty}</p>
                <p><strong>Bio:</strong> {doctor.bio}</p>
            </div>
        </div>
    );
}