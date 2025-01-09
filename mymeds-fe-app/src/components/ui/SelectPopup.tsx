import classes from "./SelectPopup.module.css";
import CloseIcon from "@mui/icons-material/Close";
import { SelectPopupItem } from "../../model/ui/SelectPopupItem";
import SearchBar from "./SearchBar";
import { useState } from "react";

const SelectPopup: React.FC<{
  name: string;
  onSelect: (value: SelectPopupItem) => void;
  onClose: () => void;
  list: SelectPopupItem[];
}> = ({ name, onSelect, onClose, list }) => {
  const [searchValue, setSearchValue] = useState<string>("");

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value);
  };

  return (
    <div className={classes.container}>
      <div className={classes.header}>
        <h1 className={classes.popupName}>{name}</h1>
        <CloseIcon className={classes.close} onClick={() => onClose()} />
      </div>
      <ul className={classes.list}>
        {list.map((option) => (
          <li
            key={option.id}
            className={classes.listItem}
            onClick={() => {
              onSelect({ id: option.id, value: option.value });
              onClose();
            }}
          >
            {option.value}
          </li>
        ))}
      </ul>
      {list.length > 5 && (
        <SearchBar
          value={searchValue}
          onChange={handleInputChange}
          onClear={() => setSearchValue("")}
          onSearch={() => {}}
        />
      )}
    </div>
  );
};

export default SelectPopup;
