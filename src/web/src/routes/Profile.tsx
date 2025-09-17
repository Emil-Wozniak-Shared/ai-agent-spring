import React, { useEffect, useMemo } from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  fetchAllUsers,
  createUser,
  updateUser,
  describeUser,
  emptyUser,
  type User,
} from "../store/slices/userSlice";
import {
  defaultOrcid,
  findOrcid,
  updateOrcid,
  type Orcid,
} from "../store/slices/orcidSlice";
import { Label } from "@radix-ui/react-label";
import { Input } from "~/components/ui/input";
import { Button } from "~/components/ui/button";
import UserCard from "~/components/user/UserCard";
import { addNotification } from "~/store/slices/appSlice";
import { useParams } from "react-router";
import { searchPubmedArticles, type PubmedArticle } from "~/store/slices/pubmedSlice";

const Profile = () => {
  const dispatch = useAppDispatch();
  const { name } = useParams();
  const { users } = useAppSelector((state) => state.users);
  const { orcid } = useAppSelector((state) => state.orcid);
  const { articles } = useAppSelector((state) => state.pubmed);

  if (users.length === 0) {
    return <div>Loading...</div>;
  }

  const user = users.find((it) => it.name == name)!!;

  if (orcid.id === null) {
    return (
      <section id="profiles">
        <UserProfile user={user!!} />
        <OrcidProfile user={user} orcid={orcid} />
      </section>
    );
  }

  const getDescription = async () => { await dispatch(describeUser()) }

  return (
    <section id="profiles">
      <UserProfile user={user!!} />
      {orcid !== null && (
        <>
          <Button className="btn btn-primary" onClick={(event) => getDescription()}>Describe</Button>
          <OrcidProfile user={user} orcid={orcid} />
          <Publications orcid={orcid} articles={articles} />
        </>
      )}
    </section>
  );
};

const Publications = ({ orcid, articles }: { orcid: Orcid, articles: PubmedArticle[] }) => {
  const dispatch = useAppDispatch();
  useEffect(() => {
      if (orcid.id !== null && articles.size === 0) {
        dispatch(
          searchPubmedArticles({
            query: orcid.id!!,
            email: orcid.email,
            maxResults: 20,
          }),
        );
      }
  }, []);

  const fetchDocs = async () => {
    try {
      const payload = {
        query: orcid.id!!,
        email: orcid.email,
        maxResults: 20,
      };
      await dispatch(searchPubmedArticles(payload)).unwrap();
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to fetch documents",
          type: "error",
        }),
      );
    }
  };
  return (
    <section id="publications">
      <ul className="rounded-lg shadow divide-y divide-gray-200 max-w-sm">
        {articles.map((article) => (
          <li key={article.id} className="px-6 py-4">
            <h4 className="font-semibold text-lg pr-2">{article.id}</h4>
            <div className="flex justify-between">
              <span className="text-gray-500 text-md">{article.title}</span>
            </div>
            <p className="text-gray-700">{article.abstract}</p>
          </li>
        ))}
      </ul>
      <div>
        <Button onClick={fetchDocs}>Fetch Publications</Button>
      </div>
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

const OrcidProfile = ({ user, orcid }: { user: User; orcid: Orcid }) => {
  const dispatch = useAppDispatch();
  const [orcidState, setOrcidState] = React.useState(orcid);
  useEffect(() => {
    if (orcid.id !== null) {
      dispatch(
        searchPubmedArticles({
          query: orcid.id!!,
          email: orcid.email,
          maxResults: 20,
        }),
      );
    }
  }, []);

  const sendUpdateOrcid = (e: React.FormEvent) => {
    if (orcidState.id !== null) {
      e.preventDefault();
      dispatch(updateOrcid(orcidState.id!!));
    }
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
        className="shadow-md rounded px-8 pt-2 mb-2"
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
            {"Update"}
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
