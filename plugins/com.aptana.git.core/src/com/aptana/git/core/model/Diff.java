		String l = null;
		while ((l = buffReader.readLine()) != null)
			if (l.length() == 0)
				continue;
			char firstChar = l.charAt(0);
			if (firstChar == 'd' && l.charAt(1) == 'i')
			{ // "diff", i.e. new file, we have to reset everything
				header = true; // diff always starts with a header

				// Finish last file
				if (!readPrologue)
					readPrologue = true;
				else
					files.add(new Diff(commit, binary, startname, endname));
				startname = ""; //$NON-NLS-1$
				endname = ""; //$NON-NLS-1$
				binary = false;

				Matcher m = DIFF_GIT_PATTERN.matcher(l);
				if (m.find())
				{ // there are cases when we need to capture filenames from
					startname = m.group(2); // the diff line, like with mode-changes.
					endname = m.group(4); // this can get overwritten later if there is a diff or if
				} // the file is binary

				continue;
			}
			if (!header)
				continue;
			switch (firstChar)
			{
				case 'n':
					Matcher m = NEW_FILE_MODE_PATTERN.matcher(l);
						startname = DEV_NULL;

					m = NEW_MODE_PATTERN.matcher(l);
					break;
				case 'o':
					m = OLD_MODE_PATTERN.matcher(l);
					break;
				case 'd':
					m = DELETED_FILE_MODE_PATTERN.matcher(l);
					if (m.find())
						endname = DEV_NULL;
					break;
				case '-':
					m = MINUS_PATTERN.matcher(l);
					if (m.find())
						startname = m.group(2);
					break;
				case '+':
					m = PLUS_PATTERN.matcher(l);
					if (m.find())
						endname = m.group(2);
					break;
				case 'r':
					// If it is a complete rename, we don't know the name yet
					// We can figure this out from the 'rename from.. rename to.. thing
					m = RENAME_PATTERN.matcher(l);
					if (m.find())
					{
						if (m.group(1).equals("from")) //$NON-NLS-1$
						else
					}
					break;
				case 'B':
					binary = true;
					// We might not have a diff from the binary file if it's new.
					// So, we use a regex to figure that out
					m = BINARY_FILES_DIFFER_PATTERN.matcher(l);
					if (m.find())
					{
						startname = m.group(2);
						endname = m.group(4);
					}
					break;
				case '@': // Finish the header
					header = false;
					break;
				default:
					break;
		files.add(new Diff(commit, binary, startname, endname));
		log(MessageFormat.format(
				"Took {0}ms to parse out {1} diffs", (System.currentTimeMillis() - start), files.size())); //$NON-NLS-1$
			GitPlugin.logInfo(string);
			return parse(gitCommit, new StringReader(result.getMessage()));