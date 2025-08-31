import type { FC } from "react";
import {
  setCurrentArticle,
  type PubmedArticle,
} from "~/store/slices/pubmedSlice";
import { Button } from "../ui/button";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "../ui/card";

type Props = {
  article: PubmedArticle;
  id: number;
};

const PubmedArticleCard: FC<Props> = ({ article, id }) => {
  const dispatch = useAppDispatch();
  const { currentArticle } = useAppSelector((state) => state.pubmed);
  return (
    <Card key={article.id ?? id} className="article-card w-full max-w-sm">
      <CardHeader>
        <CardTitle>
          <>{article.title}</>
        </CardTitle>
        <CardAction>
          <Button
            onClick={() => dispatch(setCurrentArticle(article))}
            className={currentArticle?.id === article.id ? "active" : ""}
            variant={
              currentArticle?.id === article.id ? "default" : "secondary"
            }
          >
            {currentArticle?.id === article.id ? "Selected" : "Select"}
          </Button>
        </CardAction>
      </CardHeader>
      <CardContent>
        <p>
          <strong>Source:</strong> {article.source}
        </p>
        <p>
          <strong>Published:</strong>{" "}
          {new Date(article.pubDate).toLocaleDateString()}
        </p>
        {article.doi && (
          <p>
            <strong>DOI:</strong> {article.doi}
          </p>
        )}

        {article.abstract && (
          <div className="abstract">
            <h4>Abstract:</h4>
            <p>{article.abstract}</p>
          </div>
        )}
      </CardContent>
    </Card>
  );
};

export default PubmedArticleCard;
