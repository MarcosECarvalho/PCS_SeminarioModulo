function isNotEnterKey(evt) 
{
	var charCode = (evt.which) ? evt.which : event.keyCode;
	
	if (charCode == 13)
	{
		return false;
	}
	
	return true;
}

function isNumberKey(evt)
{
	var charCode = (evt.which) ? evt.which : event.keyCode;

	if (charCode > 31 && (charCode < 48 || charCode > 57))
	{
		return false;
	}


	return true;
}

function isLetterKey(evt)
{
	var charCode = (evt.which) ? evt.which : event.keyCode;

	if (charCode < 31 || (charCode >= 65 && charCode <= 90) || (charCode >= 97 && charCode <= 122))
	{
		return true;
	}

	return false;
}