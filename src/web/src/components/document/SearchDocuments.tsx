import React, { type FC } from "react";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { addNotification } from "~/store/slices/appSlice";
import {
  clearSearchResults,
  searchDocuments,
} from "~/store/slices/documentSlice";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";

const SearchDocuments = () => {
  const dispatch = useAppDispatch();
  const { searchResults, loading } = useAppSelector((state) => state.documents);
  const [searchQuery, setSearchQuery] = React.useState("");
  const [newDocs, setNewDocs] = React.useState([{ text: "", metadata: "" }]);

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
    <div className="container mx-auto">
      <h2 className="text-2xl">Search Documents</h2>
      <form
        onSubmit={handleSearch}
        className="shadow rounded px-8 pt-6 pb-8 mb-4"
      >
        <div className="w-full flex flex-row">
          <Input
            type="text"
            placeholder="Search query..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            required
            className="mr-2"
          />
          <Button type="submit" disabled={loading} className="mr-2">
            {loading ? "Searching..." : "Search"}
          </Button>
          <Button
            type="button"
            onClick={() => dispatch(clearSearchResults())}
            disabled={searchResults.length === 0}
          >
            Clear Results
          </Button>
        </div>
      </form>

      {searchResults.length > 0 && (
        <div className="search-results">
          <h3 className="text-xl">Search Results ({searchResults.length})</h3>
          {searchResults.map((doc) => (
            <div key={doc.id} className="document-card">
              <p className="text-xl">{doc.text}</p>
              {doc.metadata && <Metadata metadata={doc.metadata} />}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

type MetadataProps = {
  metadata: object;
};

const Metadata: FC<MetadataProps> = ({ metadata }) => {
  type Field = { key: string; value: object };
  const fields: Field[] = [];
  for (const [key, value] of Object.entries(metadata)) {
    fields.push({ key, value });
  }
  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button variant="outline">Metadata</Button>
      </PopoverTrigger>
      <PopoverContent className="w-4xl">
        <ul className="text-sm text-gray-600 ml-2">
          <caption>Metadata:</caption>
          {fields.map((entry) => (
            <li key={`${entry.key}-${entry.value}`}>
              {`${entry.key}`}: {`${entry.value}`}
            </li>
          ))}
        </ul>
      </PopoverContent>
    </Popover>
  );
};

export default SearchDocuments;
