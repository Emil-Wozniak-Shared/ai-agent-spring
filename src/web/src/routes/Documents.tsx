import React from "react";
import { useAppSelector, useAppDispatch } from "../store/hooks";
import {
  searchDocuments,
  createManyDocuments,
  clearSearchResults,
} from "../store/slices/documentSlice";
import { addNotification } from "../store/slices/appSlice";
import CreateDocumentForm from "~/components/document/CreateDocumentForm";
import SearchDocuments from "~/components/document/SearchDocuments";

const Documents: React.FC = () => {
  const dispatch = useAppDispatch();
  const { documents, searchResults, loading, error } = useAppSelector(
    (state) => state.documents,
  );
  const [searchQuery, setSearchQuery] = React.useState("");
  const [newDocs, setNewDocs] = React.useState([{ title: "", content: "" }]);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    try {
      await dispatch(searchDocuments({ query: searchQuery })).unwrap();
      dispatch(
        addNotification({
          message: "Search completed successfully!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Search failed",
          type: "error",
        }),
      );
    }
  };

  return (
    <>
      <h1 className="text-4xl">Documents</h1>
      <SearchDocuments />

      {error && <div className="error-message">{error}</div>}
    </>
  );
};

export default Documents;
