#!/usr/bin/env python

############################
# csv_report.py            #
# Author: dk1024           #
############################


'''In this program, I will ask you for a CSV file,
which should contain information on events attended by people.
I expect the CSV file to be of the following format:

[Name of the person],[Time spent on the event],[Location of the event]

Each line, or row, of the file should represent 
one event attended by one particular person.

Once I read this file, I will print out
the number of events attended by each individual 
and his/her average units of time spent per event.'''



def main():
    
	print('\n' + __doc__ + '\n')
	
	while 1:
		try:
			try:
				# Getting the inputs could also be done via command line arguments.
				# The problem with doing that is the fact that the "sys.args" list
				# considers characters separated by spaces different entries. Hence,
				# processing of arguments could require some complicated text analysis.
				# It's much simpler to just run this script and type in the filepath 
				# when the program asks for it.

				filepath = get_filepath()
				if not filepath:
					# Time to exit!
					break
				
				decimal_place = get_decimal_place()
				if not decimal_place:
					# Time to exit!
					break
				
				print_feedback(get_data_dict(filepath), decimal_place)
			
			except FileNotFoundError:
				# This can happen easily, so I chose not to raise it.
				#
				print('\nSorry, I couldn\'t find your file!\
						\nEither the filepath entered was wrong,\
						\nor the file does not exist in the given directory.\
						\nPlease try again.\n')

			are_we_done = input('\nAre you done?\
								\nEnter \'Y\' or \'y\' to exit,\
								\nor make any other entry to keep going: ')
			if are_we_done=='y' or are_we_done=='Y':
				break
		except:
			# If the program gets here, then something completely unexpected has happened!
			#
			print('\nUnexpected Error!\n')
			raise # Definitely need this, just to be safe!
	
	print('\nGOODBYE!\n')


def get_filepath():
	while True:
		filepath = input('\nPlease enter the complete path of the CSV file\
						 (or make a blank entry to exit):\n')
		if filepath=='':
			# Program ends.
			break

		print('You entered: "' + filepath + '"')
		
		if filepath.endswith('.csv'):
			break
		else:
			print('\nThat is not a csv file.')
	return filepath


def get_decimal_place():
	n = 0
	print('\nI may need to round off some of the results.\
			\nHow many decimal places should I choose?')
	while 1:
		n = input('Please enter an integer (or make a blank entry to exit): ')

		if n=='':
			# Program ends.
			break

		if n.isnumeric():
			print('The no. of decimal places you have chosen = ' + n)
			break
		else:
			print('\n' + n + ' is not an integer!')
	return n


def increment(how_much):
	# This is a fancier way of incrementing.
	# Not necessary, but does facilitate better modularization.
	# Plus, I think they are cool!
	return lambda x: x + how_much


def get_data_dict(filepath):
	d = {}
	# I have used a dictionary here because while we don't want the names
	# to change (which will be the immutable keys), we do want the
	# statistics (which will be the 2-item lists) on each person to
	# be mutable.  The dictionary will also provide excellent complexity:
	# 	-> O(1) to set an item (avg)
	#	-> O(1) to get an item (avg)

	with open(filepath, 'r') as file:
		##
		# The with statement will ensure proper clean-up (e.g. closing the file)
		# even when there's a problem performing any of the tasks within this code-block.
		# 
		for line in file:
			l = line.rstrip('\n').split(sep=',') # Cleans up the data
			
			if l[0] not in d.keys():
				d[l[0]] = [float(l[1]), 1] 
				# Floats will ensure closer approximations and 
				# let the user customize accuracy.
			else:
				inc_total_time = increment(float(l[1]))
				inc_num_events = increment(1)
				d[l[0]] = [inc_total_time(d[l[0]][0]), inc_num_events(d[l[0]][1])]

		return d


def print_feedback(d, n):
	print('\n')
	output = '{person} has been to {no_of_events} events at {avg_time} units of time per event.'
	for k,v in d.items():
		print(output.format(person = k, no_of_events = v[1], avg_time = round(v[0]/v[1], int(n))))



#-------------------------------------------------------------------------

if __name__ == '__main__':
	main()
