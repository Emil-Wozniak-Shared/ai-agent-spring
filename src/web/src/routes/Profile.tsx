import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
  type User,
  emptyUser,
} from "../store/slices/userSlice";
import { updateOrcid } from "../store/slices/orcidSlice";
import { Label } from "@radix-ui/react-label";
import { Input } from "~/components/ui/input";
import { Button } from "~/components/ui/button";
import UserCard from "~/components/user/UserCard";
import { addNotification } from "~/store/slices/appSlice";
import { useParams } from "react-router";

const Profile = () => {
  const dispatch = useAppDispatch();
  const { name } = useParams();
  const { users, loading, error } = useAppSelector((state) => state.users);

  const user = users.find((it) => it.name == name)!!;
  return (
    <section id="profiles">
      <UserProfile user={user!!} />
      <OrcidProfile user={user} />
    </section>
  );
};

const UserProfile = ({ user }: { user: User }) => {
  const dispatch = useAppDispatch();

  const [editingUser, setEditingUser] = React.useState<string | null>(null);
  const handleUpdateUser = async (email: string, userData: User) => {
    try {
      await dispatch(updateUser({ email, userData })).unwrap();
      setEditingUser(null);
      dispatch(
        addNotification({
          message: "User updated successfully!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to update user",
          type: "error",
        }),
      );
    }
  };
  return (
    <section className="mt-2">
      <UserCard
        key={user.email}
        user={user}
        isEditing={editingUser === user.email}
        onEdit={() => setEditingUser(user.email!!)}
        onSave={(userData) => handleUpdateUser(user.email!!, userData)}
        onCancel={() => setEditingUser(null)}
      />
    </section>
  );
};

const OrcidProfile = ({ user }: { user: User }) => {
  const dispatch = useAppDispatch();
  const { orcid, loading, error } = useAppSelector((state) => state.orcid);
  const [orcidState, setOrcidState] = React.useState(orcid);
  const sendUpdateOrcid = (e: React.FormEvent) => {
    e.preventDefault();
    dispatch(updateOrcid(orcidState.id!!));
  };

  let url = new URL("https://orcid.org/orcid-search/search");

  // Adding query parameters
  let params = new URLSearchParams(url.search);
  params.append("firstName", user.firstName);
  params.append("lastName", user.lastName);

  // Updating the URL with encoded query parameters
  url.search = params.toString();

  return (
    <section id="orcid-section">
      <form
        onSubmit={sendUpdateOrcid}
        className="shadow-md rounded px-8 pt-6 pb-8 mb-4"
      >
        <div className="mb-6 flex flex-row">
          <Label htmlFor="orcid-id">Orcid ID:</Label>
          <Input
            id="orcid-id"
            type="text"
            value={orcidState.id ?? ""}
            onChange={(e) => {
              setOrcidState({ ...orcidState, id: e.target.value });
            }}
            required
            className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
          />
          <Button type="submit" variant="outline">
            {loading ? "Updating..." : "Update"}
          </Button>
        </div>
      </form>
      <a
        target="_blank"
        rel="noopener noreferrer"
        href={url.toString()}
        className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
      >
        {"Click here if you don't remember your ORCID ID"}
      </a>
    </section>
  );
};

export default Profile;
